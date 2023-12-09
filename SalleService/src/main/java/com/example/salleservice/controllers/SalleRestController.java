package com.example.salleservice.controllers;

import com.example.salleservice.Entities.Salle;
import com.example.salleservice.services.ISalleServiceImp;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("salle")
@CrossOrigin(origins = "*")
@Slf4j
@AllArgsConstructor
public class SalleRestController {
    private final ISalleServiceImp iSalleServiceImp;

    @PostMapping(value = "/addSalle", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Salle> addSalle(
            @RequestParam("image") MultipartFile image,
            @RequestParam("name") String name,
            @RequestParam("Location")  String location,
            @RequestParam("capacity") int capacity
    )throws IOException, SQLException {

        Salle salle = new Salle();
        salle.setName(name);
        salle.setCapacity(capacity);
        salle.setLocation(location);
        if (!image.isEmpty()) {
            byte[] fileBytes = image.getBytes();

            // Convert the byte array to a Blob
            Blob blob = new SerialBlob(fileBytes);

            // Set the Blob to the Product's imageUrl property
            salle.setImage(blob);

            Salle addedSalle = iSalleServiceImp.addSalle(salle);
            return new ResponseEntity<>(addedSalle, HttpStatus.CREATED);
        } else {
            // Handle case where no image is uploaded
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);        }
    }

    @PutMapping
    public ResponseEntity<Salle> updateSalle(@RequestBody Salle salle) {
        Salle updatedSalle = iSalleServiceImp.updateSalle(salle);
        return new ResponseEntity<>(updatedSalle, HttpStatus.OK);
    }

    @GetMapping("{idSalle}")
    public ResponseEntity<Salle> retrieveSalle(@PathVariable long idSalle) {
        Salle retrievedSalle = iSalleServiceImp.retrieveSalleById(idSalle);
        return new ResponseEntity<>(retrievedSalle, HttpStatus.OK);
    }

    @DeleteMapping("{idSalle}")
    public ResponseEntity<Void> removeSalle(@PathVariable long idSalle) {
        iSalleServiceImp.removeSalle(idSalle);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<Salle>> retrieveSalles() {
        List<Salle> salles = iSalleServiceImp.retrieveSalles();
        return new ResponseEntity<>(salles, HttpStatus.OK);
    }
}
