package com.tjg_project.candy.domain.analytics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjg_project.candy.domain.analytics.dto.ReviewAnalysisDto;
import com.tjg_project.candy.domain.openai.service.OpenAiService;
import com.tjg_project.candy.domain.product.entity.ProductReview;
import com.tjg_project.candy.domain.product.repository.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewAnalysisService {

    private final ProductReviewRepository reviewRepository;
    private final OpenAiService openAiService;
    private final ObjectMapper mapper = new ObjectMapper();

    public ReviewAnalysisDto analyzeReviews(Long ppk) {
        List<ProductReview> reviews = reviewRepository.findByPpk(ppk);

        if (reviews.isEmpty()) {
            throw new RuntimeException("리뷰가 없습니다.");
        }

        String productName = reviews.get(0).getProductName();

        // 리뷰 텍스트 모으기
        StringBuilder sb = new StringBuilder();
        for (ProductReview r : reviews) {
            sb.append("- ").append(r.getContent()).append("\n");
        }

        // GPT 프롬프트
        String prompt = """
        아래는 특정 상품의 리뷰 목록이다.

        ⚠️ 반드시 아래 JSON 형식만 출력해라. 설명 문장 절대 포함하지 마라.
        JSON 결과 외의 텍스트는 절대 넣지 마라.

        {
          "tasteKeywords": ["string"],
          "positivePoints": ["string"],
          "qualityIssues": ["string"],
          "positiveCount": 0,
          "negativeCount": 0
        }

        분석 기준:
        - 맛 관련 키워드 3~5개
        - 고객들이 좋아하는 포인트 3~5개
        - 품질 문제(상함, 파손, 양 적음 등) 3개
        - 긍정/부정 리뷰 개수 계산
        - 한국어 키워드 사용

        리뷰 목록:
        """ + sb;

        String raw = openAiService.ask(prompt);

        System.out.println("GPT 응답(JSON): " + raw);

        ReviewAnalysisDto dto;

        try {
            dto = mapper.readValue(raw, ReviewAnalysisDto.class);
        } catch (Exception e) {
            throw new RuntimeException("GPT JSON 파싱 실패: " + raw);
        }

        dto.setPpk(ppk);
        dto.setProductName(productName);

        return dto;
    }
}
