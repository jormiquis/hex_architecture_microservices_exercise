package edu.uoc.epcsd.productcatalog.application.rest;


import java.net.URI;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.uoc.epcsd.productcatalog.application.rest.request.CreateItemRequest;
import edu.uoc.epcsd.productcatalog.domain.Item;
import edu.uoc.epcsd.productcatalog.domain.ItemStatus;
import edu.uoc.epcsd.productcatalog.domain.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/items")
public class ItemRESTController {


    private final ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Item> getAllItems() {
        log.trace("getAllItems");

        return itemService.findAllItems();
    }

    @GetMapping("/{serialNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Item> getItemById(@PathVariable @NotBlank String serialNumber) {
        log.trace("getItemById");

        return itemService.findBySerialNumber(serialNumber).map(item -> ResponseEntity.ok().body(item))
                .orElse(ResponseEntity.notFound().build());
    }

    /*
     * Sets a product unit as operational and available for rent again
     */
    @PatchMapping("/{serialNumber}")
    public ResponseEntity<Item> setOperational(
        @PathVariable @NotBlank String serialNumber,
        @RequestBody @NotNull boolean operational
    ) {
        log.trace("setOperational");

            log.trace("Switching item with serial number  " + serialNumber + " to " + operational + " status");

            Item item = itemService
                .findBySerialNumber(serialNumber)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"The item " + serialNumber + " does not exist"));
            ItemStatus status =
                operational
                ? ItemStatus.OPERATIONAL
                : ItemStatus.NON_OPERATIONAL;

            item.setStatus(status);

            return ResponseEntity.ok(item);
        }

    @PostMapping
    public ResponseEntity<String> createItem(@RequestBody @NotNull @Valid CreateItemRequest createItemRequest) {
        log.trace("createItem");

        try {
            log.trace("Creating item " + createItemRequest);
            String serialNumber = itemService.createItem(createItemRequest.getProductId(),
                    createItemRequest.getSerialNumber());
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{serialNumber}")
                    .buildAndExpand(serialNumber)
                    .toUri();

            return ResponseEntity.created(uri).body(serialNumber);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The specified product id " + createItemRequest.getProductId() + " does not exist.", e);
        }
    }
}
