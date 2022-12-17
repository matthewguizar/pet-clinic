package com.springframework.controllers;

import com.springframework.model.Pet;
import com.springframework.model.Visit;
import com.springframework.repositories.PetRepository;
import com.springframework.repositories.VisitRepository;
import com.springframework.services.PetService;
import com.springframework.services.VisitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
public class VisitController {

    private final String CREATE_OR_UPDATE_VISIT_FORM= "pets/createOrUpdateVisitForm";
    private final VisitService visitService;
    private final PetService petService;


    public VisitController(VisitService visitService, PetService petService) {
        this.visitService = visitService;
        this.petService = petService;

    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("visit")
    public Visit loadPetWithVisit(@PathVariable("petId") Long petId, Model model){
        Pet pet = petService.findById(petId);

        model.addAttribute("pet", pet);
        Visit visit = new Visit();
        pet.getVisits().add(visit);
        visit.setPet(pet);
        return visit;
    }

    @GetMapping("/owners/*/pets/{petId}/visits/new")
    public String initNewVisitForm(@PathVariable("petId") Long petId, Model model) {
        return CREATE_OR_UPDATE_VISIT_FORM;

    }

    @PostMapping("/owners/{ownerId}/pets/{petId}/visits/new")
    public String processNewVisitForm(@Validated Visit visit, BindingResult result) {
        if (result.hasErrors()) {
            return CREATE_OR_UPDATE_VISIT_FORM;
        } else {
            visitService.save(visit);
            return "redirect:/owners/{ownerId}";
        }
    }

}
