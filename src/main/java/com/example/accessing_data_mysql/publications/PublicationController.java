package com.example.accessing_data_mysql.publications;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.accessing_data_mysql.users.Role;
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

        if(author.getRole() != Role.PUBLISHER) {
            throw new IllegalArgumentException("Only publishers can create publications.");
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

    @GetMapping(path="/update")
    public ResponseEntity<Publication> updatePublication(@RequestParam Integer id, @RequestParam Optional<String> title, @RequestParam Optional<String> content, @RequestParam Integer authorId) {
        Publication publication = publicationRepository.findById(id).orElse(null);
        if (publication == null) {
            return ResponseEntity.notFound().build();
        }
        User author = userRepository.findById(authorId).orElse(null);
        if (author == null) {
            throw new IllegalArgumentException("Invalid author ID: " + authorId); 
        }

        if (publication.getAuthor().getRole() != Role.PUBLISHER) {
            throw new IllegalArgumentException("Only publishers can update their own publications.");
        }

        if (publication.getAuthor().getId() != authorId) {
            throw new IllegalArgumentException("Author ID does not match the publication's author.");
        }

        if (title.isPresent()) {
            publication.setTitle(title.get());
        }
        if (content.isPresent()) {
            publication.setContent(content.get());
        }
        publicationRepository.save(publication);
        
        return ResponseEntity.ok(publication);
    }
    @GetMapping(path="/delete")
    public ResponseEntity<?> deletePublication(@RequestParam Integer id, @RequestParam Integer authorId) {
        Publication publication = publicationRepository.findById(id).orElse(null);
        if (publication == null) {
            return ResponseEntity.notFound().build();
        }
        User author = userRepository.findById(authorId).orElse(null);
        if (author == null) {
            throw new IllegalArgumentException("Invalid author ID: " + authorId); 
        }
        if (publication.getAuthor().getRole() != Role.PUBLISHER) {
            throw new IllegalArgumentException("Only publishers can delete their own publications.");
        }
        if (publication.getAuthor().getId() != authorId) {
            throw new IllegalArgumentException("Author ID does not match the publication's author.");
        }
        publicationRepository.delete(publication);
        return ResponseEntity.ok().build();
    }
    /**
     * Get all publications.
     */
	@GetMapping(path="/all")
	public @ResponseBody Iterable<Publication> getPosts() {
		return publicationRepository.findAll();
	}

    /**
     * Get publications by author ID.
     */
    @GetMapping(path = "/author")
    public @ResponseBody Iterable<Publication> getPublicationsByAuthor(
            @RequestParam Integer authorId) {

        userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid author ID: " + authorId));

        return publicationRepository.findByAuthorId(authorId);
    }

    @GetMapping(path="/moderator/delete")
    public ResponseEntity<?> deletePublicationModerator(@RequestParam Integer id, @RequestParam Integer authorId) {
        Publication publication = publicationRepository.findById(id).orElse(null);
        if (publication == null) {
            return ResponseEntity.notFound().build();
        }
        User author = userRepository.findById(authorId).orElse(null);
        if (author == null) {
            throw new IllegalArgumentException("Invalid author ID: " + authorId); 
        }
        if (author.getRole() != Role.MODERATOR) {
            throw new IllegalArgumentException("Only moderators can delete publications.");
        }
        publicationRepository.delete(publication);
        return ResponseEntity.ok().build();
    }
}

