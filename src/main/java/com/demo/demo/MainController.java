package com.demo.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"upstream", "list"})
public class MainController {
    
    @Autowired
    private PeerService peerService;

    @RequestMapping("/")
    public String showMainPage(String stream, Model model) {

        if(stream == null) {
            List<String> list =  new ArrayList<>();
            list.add("demo-backend");
            list.add("trac-backend");

            model.addAttribute("list", list);
            return "index";
        }
        Upstream upstream = peerService.getUpstream(stream);
        
        model.addAttribute("upstream", upstream);
        return "index";
    }

    @RequestMapping(value = "/down", params = "action=down")
    public String down(@ModelAttribute("upstream") Upstream upstream, Model model) {
        System.out.println("down");
            for(Peer peer : upstream.getPeers()) {
               if(peer.isArg()) peerService.downPeer(peer, upstream.getZone()); 
            }

            return "redirect:/";
    }

    @RequestMapping(value = "/down" , params = "action=drain")
    public String drain(@ModelAttribute("upstream") Upstream upstream, Model model) {
            System.out.println("drain");
            for(Peer peer : upstream.getPeers()) {
               if(peer.isArg()) peerService.drainPeer(peer, upstream.getZone()); 
            }

            return "redirect:/";
    }
}
