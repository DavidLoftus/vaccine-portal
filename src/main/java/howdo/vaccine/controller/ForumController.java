package howdo.vaccine.controller;

import howdo.vaccine.model.ForumPost;
import howdo.vaccine.model.ForumThread;
import howdo.vaccine.model.NewThread;
import howdo.vaccine.model.User;
import howdo.vaccine.repository.ForumPostRepository;
import howdo.vaccine.repository.ForumThreadRepository;
import howdo.vaccine.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(path = "/forum")
public class ForumController {

    private static final Logger forumLogger = LogManager.getLogger(ForumController.class);


    @Autowired
    private ForumThreadRepository threadRepo;

    @Autowired
    private ForumPostRepository postRepo;

    @ModelAttribute("page")
    public String getPage() {
        return "forum";
    }

    @GetMapping("")
    public String getQuestions(Model model, @ModelAttribute NewThread newThread) {
        List<ForumThread> threads = threadRepo.findAll();

        model.addAttribute("threads", threads);

        return "forum";
    }


    @Autowired
    private UserService userService;

    @PostMapping("/new")
    public void postNewQuestion(HttpServletResponse response,
                                @Valid @ModelAttribute NewThread newThread) throws IOException {
        User currentUser = userService.getCurrentUser();

        ForumThread thread = new ForumThread();
        thread.setAuthor(currentUser);
        thread.setTitle(newThread.getTitle());

        thread = threadRepo.save(thread);

        ForumPost post = new ForumPost();
        post.setContent(newThread.getContent());
        post.setDateCreated(new Date());
        post.setThread(thread);
        post.setAuthor(currentUser);

        postRepo.save(post);

        ForumController.forumLogger.info("User \"" + userService.getCurrentUser().getId() + "\" has posted a new question (Post ID: " + post.getId() + ")");

        response.sendRedirect("/forum/" + thread.getId());
    }

    @GetMapping("/{id}")
    public String getThread(@PathVariable("id") long id, Model model) {
        model.addAttribute("thread", threadRepo.getOne(id));
        model.addAttribute("id", id);
        model.addAttribute("user", userService.getCurrentUser());
        return "forum_thread";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}")
    public void newComment(HttpServletResponse response, @PathVariable("id") long id, @RequestParam String content, Model model) throws IOException {
        ForumThread thread = threadRepo.getOne(id);

        ForumPost post = new ForumPost();
        post.setAuthor(userService.getCurrentUser());
        post.setContent(content);
        post.setThread(thread);
        post.setDateCreated(new Date());

        postRepo.save(post);

        ForumController.forumLogger.info("User \"" + userService.getCurrentUser().getId() + "\" has posted a new comment (ID: " + post.getId() + ") for thread: (ID: " + thread.getId() + ")");


        response.sendRedirect("/forum/" + thread.getId());
    }

}
