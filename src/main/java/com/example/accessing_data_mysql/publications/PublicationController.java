package com.example.accessing_data_mysql.publications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.accessing_data_mysql.users.User;
import com.example.accessing_data_mysql.users.UserRepository;

@Controller
@RequestMapping(path="/posts")
public class PublicationController {
    @Autowired
	private PublicationRepository publicationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping(path="/add")
    public @ResponseBody Publication addNewPublication(@RequestParam String title, @RequestParam String content, @RequestParam Integer authorId) {
        User author = userRepository.findById(authorId).orElse(null);
        
        if (author == null) {
            throw new IllegalArgumentException("Invalid author ID: " + authorId); 
        }
        
        Publication p = new Publication();
        p.setTitle(title);
        p.setContent(content);
        p.setAuthor(author);
        publicationRepository.save(p);
        return p;
    }

    @GetMapping(path="/get")
    public ResponseEntity<Publication> getPublicationById(@RequestParam Integer id) {
        Publication publication = publicationRepository.findById(id).orElse(null);
        if (publication == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(publication);
    }

	@GetMapping(path="/all")
	public @ResponseBody Iterable<Publication> getPosts() {
		return publicationRepository.findAll();
	}
}
