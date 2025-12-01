package com.tjg_project.candy.domain.order.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjg_project.candy.domain.openai.service.OpenAiService;
import com.tjg_project.candy.domain.order.dto.DailySalesDto;
import com.tjg_project.candy.domain.order.dto.ForecastResponseDto;
import com.tjg_project.candy.domain.order.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ForecastService {

    private final OrderDetailRepository orderDetailRepository;
    private final OpenAiService openAiService;

    public List<DailySalesDto> getDailySales(Long ppk) {
        return orderDetailRepository.findDailySalesByProduct(ppk);
    }

    public ForecastResponseDto runForecast(Long ppk) throws Exception {

        List<DailySalesDto> sales = getDailySales(ppk);

        StringBuilder sb = new StringBuilder();
        sb.append("다음은 최근 일별 판매량 데이터이다. '날짜=수량' 형식:\n");

        for (DailySalesDto s : sales) {
            sb.append(s.getDateTime()).append("=").append(s.getQty()).append("\n");
        }

        sb.append(
                "설명 금지. 코드블록 금지. JSON만 출력.\n" +
                        "반드시 아래 형식 그대로 출력해.\n" +
                        "{\n" +
                        "  \"next7Days\": [7개 정수],\n" +
                        "  \"next30Days\": [30개 정수],\n" +
                        "  \"next12Months\": [12개 정수],\n" +
                        "  \"next365Days\": [365개 정수]\n" +
                        "}\n" +
                        "모든 배열은 정확한 개수의 정수만 포함해야 한다.\n"
        );

        String raw = openAiService.ask(sb.toString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(raw);

        List<Integer> next7 = safeList(root.get("next7Days"), 7);
        List<Integer> next30 = safeList(root.get("next30Days"), 30);
        List<Integer> next12 = safeList(root.get("next12Months"), 12);
        List<Integer> next365 = safeList(root.get("next365Days"), 365);

        return new ForecastResponseDto(next7, next30, next12, next365);
    }

    // ⚠️ 안전 리스트 생성 함수 (길이 보정)
    private List<Integer> safeList(JsonNode node, int size) {
        List<Integer> list = new ArrayList<>();
        if (node != null && node.isArray()) {
            node.forEach(v -> list.add(v.asInt()));
        }

        while (list.size() < size) {
            list.add(0);
        }

        if (list.size() > size) {
            return list.subList(0, size);
        }

        return list;
    }
}
