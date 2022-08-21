package org.userway.assignment.litstull.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class OriginLinkResponseDto implements Serializable {

    private String link;

}
