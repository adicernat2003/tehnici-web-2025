package org.example.mapper;

import org.example.dto.CreateWidgetRequest;
import org.example.dto.WidgetResponse;
import org.example.model.Widget;
import org.springframework.stereotype.Component;

@Component
public class WidgetMapper {

    public WidgetResponse toResponse(Widget widget) {
        return WidgetResponse.builder()
                .name(widget.getName())
                .description(widget.getDescription())
                .build();
    }

    public Widget toEntity(CreateWidgetRequest request) {
        return Widget.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

}
