package soa.web;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
public class SearchController {

	@Autowired
	  private ProducerTemplate producerTemplate;

	@RequestMapping("/")
    public String index() {
        return "index";
    }


    @RequestMapping(value="/search")
    @ResponseBody
    public Object search(@RequestParam("q") String q) {
        Map<String,Object> header = new HashMap<>();

        //Only limits the search if q contains the substring max:X
        if(q.contains("max:")){
            String[] max = q.split("max:");
            Integer count = Integer.parseInt(max[max.length-1]);
            //Accept the search of a string that contains max:X and only limit the results
            //using de last max:X of the string
            if(max.length>2){
                q="";
                for(int i=0; i<max.length-1; i++){
                    q += max[i];
                    if(i<max.length-2){
                        q += "max:";
                    }
                }
            }else{
                q=max[0];
            }
            header.put("CamelTwitterCount",count);
        }
        header.put("CamelTwitterKeywords",q);

        return producerTemplate.requestBodyAndHeaders("direct:search", "", header);
    }
}