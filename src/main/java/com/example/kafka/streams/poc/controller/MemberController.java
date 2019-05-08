package com.example.kafka.streams.poc.controller;

import com.example.kafka.streams.poc.mongodb.entity.MemberEntity;
import com.example.kafka.streams.poc.mongodb.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Member controller.
 * Base route: /member
 */
@Controller
@RequestMapping("/member")
public class MemberController {

    /** The mongoDB repository where to retrieve the members */
    private MemberRepository memberRepository;

    /**
     * Autowired constructor
     *
     * @param memberRepository the mongoDB member repository
     */
    @Autowired
    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * GET /member
     *
     * Lists the members
     *
     * @param size  the page size (default = 15)
     * @param page  the page number (default = 0)
     * @return the model and view
     */
    @GetMapping({"", "/"})
    public ModelAndView getMembersAction(
            @RequestParam(value="size", required=false, defaultValue="15") int size,
            @RequestParam(value="page", required=false, defaultValue="0") int page
    )  {
        ModelAndView mav  = new ModelAndView("member/list");

        List<MemberEntity> members = memberRepository
                .findAll(PageRequest.of(page, size, new Sort(Sort.Direction.DESC, Arrays.asList("firstName", "lastName"))))
                .getContent();

        long count = memberRepository.count();
        long prev = (page > 0) ? page - 1 : 0;
        long next = (size * (page + 1) < count) ? page + 1 : page;

        mav.addObject("members", members);
        mav.addObject("count", count);
        mav.addObject("size", size);
        mav.addObject("page", page);
        mav.addObject("prev", prev);
        mav.addObject("next", next);

        return mav;
    }

    /**
     * GET /member/{id}
     *
     * Shows a member
     *
     * @param uuid the uuid of the member
     * @return the model and view
     */
    @GetMapping("/{id}")
    public ModelAndView getMembersAction(@PathVariable("id") String uuid) {
        ModelAndView mav  = new ModelAndView("member/show");

        Optional<MemberEntity> member = memberRepository.findById(uuid);

        mav.addObject("uuid", uuid);
        mav.addObject("member", member.orElse(null));

        return mav;
    }
}
