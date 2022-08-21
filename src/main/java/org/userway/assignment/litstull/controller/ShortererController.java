package org.userway.assignment.litstull.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.userway.assignment.litstull.model.OriginLinkResponseDto;
import org.userway.assignment.litstull.model.ShortLinkResponseDto;
import org.userway.assignment.litstull.service.ShortererService;
import org.userway.assignment.litstull.service.exceptions.BadRequestLink;
import org.userway.assignment.litstull.service.exceptions.NotFoundOriginException;
import org.userway.assignment.litstull.service.exceptions.NotFoundShortException;

@RestController
@Slf4j
@RequestMapping("listfull/v1")
public class ShortererController {

    final ShortererService shortererService;

    public ShortererController(ShortererService shortererService) {
        this.shortererService = shortererService;
    }

    @Operation(summary = "Returns short link")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns short link",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShortLinkResponseDto.class))),
            @ApiResponse(responseCode = "418", description = "Returns empty link if something gone wrong",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShortLinkResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Returns empty link if no origin link",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShortLinkResponseDto.class)))
    })
    @GetMapping("/cut")
    public ResponseEntity<ShortLinkResponseDto> getShortUrl(@RequestParam String originUrl) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        try {
            ShortLinkResponseDto responseDto = shortererService.getShort(originUrl);
            return new ResponseEntity<>(
                    responseDto,
                    httpHeaders,
                    HttpStatus.OK
            );
        } catch (NotFoundShortException e) {
            return new ResponseEntity<>(
                    new ShortLinkResponseDto(""),
                    httpHeaders,
                    HttpStatus.I_AM_A_TEAPOT
            );

        } catch (BadRequestLink e) {
            return new ResponseEntity<>(
                    new ShortLinkResponseDto(""),
                    httpHeaders,
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns origin link by short",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShortLinkResponseDto.class))),
            @ApiResponse(responseCode = "418", description = "Returns empty link if short not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShortLinkResponseDto.class)))
    })
    @GetMapping("/restore")
    public ResponseEntity<OriginLinkResponseDto> getOriginUrl(@RequestParam String shortLink) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        log.info(String.format("Request for origin link by %s", shortLink));
        try {
            OriginLinkResponseDto origin =
                    new OriginLinkResponseDto(shortererService.getOrigin(shortLink).getOrigin());
            return new ResponseEntity<>(
                    origin,
                    httpHeaders,
                    HttpStatus.OK
            );
        } catch (NotFoundOriginException exception) {
            return new ResponseEntity<>(
                    new OriginLinkResponseDto(""),
                    httpHeaders,
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        shortererService.deleteById(id);
    }

}
