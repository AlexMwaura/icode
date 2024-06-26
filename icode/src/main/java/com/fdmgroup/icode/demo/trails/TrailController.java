package com.fdmgroup.icode.demo.trails;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/trail")
public class TrailController {

	private TrailService trailService;

	public TrailController(TrailService trailService) {
		super();
		this.trailService = trailService;
	}
	
	@GetMapping()
	public String goToTrailsHomePage(Model model) {
		List<Trail> trails = trailService.findAllTrails();
		model.addAttribute("trails", trails);
		return "demo/trail-home";
	}
	

	@GetMapping("/search-trails") // Outside redirect
	public String goToSearchGoogle() {
		return "redirect:https://www.google.com/search?q=trails";
	}

	
	@GetMapping("/create")
	public String goToCreateTrailPage(Model model) {
		model.addAttribute("trail", new Trail());
		model.addAttribute("activities", trailService.findAllTrailActivities());
		return "demo/trail-create";
	}
	
	@PostMapping("/create")
	public String goToProcessNewTrail(Trail trail) {
		trailService.createNewTrail(trail);
		return "redirect:/trail";
	}
	
	@GetMapping("/find/{id}")
	public String goToViewTrailDetails(@PathVariable("id") int trailId, Model model) throws TrailNotFoundException {
		Trail foundTrail = trailService.findTrailById(trailId);
		model.addAttribute("trail", foundTrail);
		return "demo/trail-details";
	}
	
	@ExceptionHandler(TrailNotFoundException.class)
	public String toHandlePlayerNotFound(RedirectAttributes redirect, TrailNotFoundException pnfe) {
		redirect.addFlashAttribute("message", pnfe.getMessage());
		return "redirect:/trail";
	}

}
