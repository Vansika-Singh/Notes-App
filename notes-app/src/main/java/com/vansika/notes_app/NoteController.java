package com.vansika.notes_app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/*
   WHAT THIS FILE IS:
   The controller layer — handles all incoming HTTP requests for Notes.
   Every public API endpoint lives here.

   @RestController tells Spring this class handles HTTP requests
   and returns data directly (not a webpage).

   @RequestMapping("/api/notes") means every endpoint in this class
   starts with /api/notes — so we don't repeat it on every method.

   @CrossOrigin("*") allows our React frontend (running on a different
   port) to call this API. Without this, the browser blocks the request.
   This is called CORS — Cross Origin Resource Sharing.
*/
@RestController
@RequestMapping("/api/notes")
@CrossOrigin("*")
public class NoteController {

    /*
       DEPENDENCY INJECTION:
       Spring automatically creates a NoteRepository instance and
       passes it in here. We never call "new NoteRepository()" ourselves.
       This is constructor injection — the preferred way in Spring.
    */
    private final NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /*
       GET ALL NOTES:
       GET /api/notes
       Returns every note in the database as a JSON array.
       ResponseEntity<List<Note>> lets us control both the response body
       and the HTTP status code we send back.
       HttpStatus.OK = 200 — standard for successful GET requests.
    */
    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes() {
        List<Note> notes = noteRepository.findAll();
        return ResponseEntity.ok(notes);
    }

    /*
       GET ONE NOTE BY ID:
       GET /api/notes/{id}
       @PathVariable captures the {id} from the URL.
       findById() returns an Optional — it might exist, it might not.
       If found, return 200 with the note.
       If not found, return 404 Not Found.
    */
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        Optional<Note> note = noteRepository.findById(id);
        return note.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /*
       CREATE A NOTE:
       POST /api/notes
       @RequestBody tells Spring to read the request body and
       deserialise it into a Note object automatically (using Jackson).
       Returns 201 Created — the correct status code for resource creation.
    */
    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        Note savedNote = noteRepository.save(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
    }

    /*
       UPDATE A NOTE:
       PUT /api/notes/{id}
       First check if the note exists. If not, return 404.
       If yes, update the fields and save.
       Returns 200 with the updated note.
    */
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id,
                                           @RequestBody Note updatedNote) {
        Optional<Note> existing = noteRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Note note = existing.get();
        note.setTitle(updatedNote.getTitle());
        note.setContent(updatedNote.getContent());
        return ResponseEntity.ok(noteRepository.save(note));
    }

    /*
       DELETE A NOTE:
       DELETE /api/notes/{id}
       Returns 204 No Content — the correct status for successful deletes.
       There is nothing to return in the body after deletion.
    */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        if (!noteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        noteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /*
       SEARCH NOTES:
       GET /api/notes/search?keyword=something
       @RequestParam reads the "keyword" query parameter from the URL.
       Calls our custom JPQL search query in the repository.
       Returns all notes where title or content contains the keyword.
    */
    @GetMapping("/search")
    public ResponseEntity<List<Note>> searchNotes(@RequestParam String keyword) {
        List<Note> results = noteRepository.searchNotes(keyword);
        return ResponseEntity.ok(results);
    }
}