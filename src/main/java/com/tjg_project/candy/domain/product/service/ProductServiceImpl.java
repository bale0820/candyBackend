//package com.tjg_project.candy.domain.product.service;
//
//import com.tjg_project.candy.domain.order.entity.KakaoPay;
//import com.tjg_project.candy.domain.order.repository.OrderDetailRepository;
//import com.tjg_project.candy.domain.product.entity.Product;
//import com.tjg_project.candy.domain.product.entity.ProductDetailView;
//import com.tjg_project.candy.domain.product.entity.ProductQnA;
//import com.tjg_project.candy.domain.product.repository.ProductDetailViewRepository;
//import com.tjg_project.candy.domain.product.repository.ProductQnARepository;
//import com.tjg_project.candy.domain.product.repository.ProductRepository;
//import com.tjg_project.candy.domain.product.repository.ProductReviewRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.time.LocalDate;
//import java.util.*;
//
//@Service
//public class ProductServiceImpl implements ProductService {
//
//    // 상품, 속성, 이미지 구분
//    private final static int PRODUCT_IMAGES = 0;
//    private final static int PRODUCT_INFORMATION = 1;
//    private final static int PRODUCT_DESCRIPTION = 2;
//
//    // 파일 업로드(application.yml의 file)
//    @Value("${file.upload-dir}")
//    private String uploadDir;
//
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private ProductReviewRepository productReviewRepository;
//    @Autowired
//    private ProductQnARepository productQnARepository;
//    @Autowired
//    private ProductDetailViewRepository productDetailViewRepository;
//    @Autowired
//    private OrderDetailRepository orderDetailRepository;
//
//    @Override
//    public List<Product> getProductList() {
//        return productRepository.findAll();
//    }
//
//    @Override
//    public List<Map<String, Object>> getProductReviewList() {
//        return productReviewRepository.findAllReviewWithUserName();
//    }
//
//    @Override
//    public List<Map<String, Object>> getProductProductQnAList() {
//        return productQnARepository.findAllProductQnAWithUserName();
//    }
//
//    @Override
//    public List<Product> getProductProductBestList() {
//        return orderDetailRepository.findBestSellingProducts();
//    }
//
//    @Override
//    public Optional<ProductDetailView> getProductDetail(Long id) {
//        return productDetailViewRepository.findById(id);
//    }
//
//    @Override
//    // 상품 정보 등록
//    public Product saveProduct(Product product, List<MultipartFile> files) {
//        // 이미지 정보 설정
//        for (int i = 0; i < files.size(); i++) {
//            MultipartFile file = files.get(i);
//            setImages(product, file, i);
//        }
//
//        // 핫딜 정보 설정(DC값이 설정되있으면 true 아니면 false)
//        product.setHotDeal(product.getDc() != 0);
//        // 등록 날짜
//        product.setProductDate(LocalDate.now());
//
//        // product테이블에 등록
//        return productRepository.save(product);
//    }
//
//    @Override
//    public Product updateProduct(Product product, List<MultipartFile> files) {
//        // product 테이블의 상품 취득(id)
//        Product findProduct = productRepository.findById(product.getId())
//                .orElseThrow(() -> new RuntimeException("상품 없음"));
//
//        // 입력 받은 데이터로 변경
//        findProduct.setAllergyInfo(product.getAllergyInfo());
//        findProduct.setBrandName(product.getBrandName());
//        findProduct.setCount(product.getCount());
//        findProduct.setDc(product.getDc());
//        findProduct.setDelType(product.getDelType());
//        findProduct.setDescription(product.getDescription());
//        findProduct.setNotes(product.getNotes());
//        findProduct.setOrigin(product.getOrigin());
//        findProduct.setPrice(product.getPrice());
//        findProduct.setProductName(product.getProductName());
//        findProduct.setSeller(product.getSeller());
//        findProduct.setUnit(product.getUnit());
//        findProduct.setWeight(product.getWeight());
//        findProduct.setCategorySub(product.getCategorySub());
//
//        // 이미지 정보 설정
//        for (int idx = 0; idx < files.size(); idx++) {
//            MultipartFile file = files.get(idx);
//            setImages(findProduct, file, idx);
//        }
//
//        // 핫딜 정보 설정(DC값이 설정되있으면 true 아니면 false)
//        findProduct.setHotDeal(product.getDc() != 0);
//        // 등록 날짜
//        findProduct.setProductDate(LocalDate.now());
//
//        // product테이블에 등록
//        return productRepository.save(findProduct);
//    }
//
//    @Override
//    // 상품 정보 삭제
//    public boolean deleteProduct(Long id) {
//        try{
//            productRepository.deleteById(id) ;
//            return true;
//        } catch (EmptyResultDataAccessException e) {
//            return false;
//        }
//    }
//
//    @Override
//    public ProductQnA addProductQnA(ProductQnA qna) {
//        qna.setStatus("답변대기");
//        return productQnARepository.save(qna);
//    }
//
//    // 이미지 정보 설정
//    public void setImages(Product product, MultipartFile file, int idx){
//        // 파일이 null이거나 비어있으면 처리하지 않음
//        if (file == null || file.isEmpty()) {
//            return;
//        }
//
//        // 파일명 취득(업로드시 파일명)
//        String originalFilename = file.getOriginalFilename();
//        // 파일명 중복방지 UUID_기존파일명
//        String filename = UUID.randomUUID() + "_" + originalFilename;
//        // 파일명 변경
//        String uploadFileDir = uploadDir;
//
//        // 상품, 속성, 이미지 구분
//        switch (idx) {
//            case PRODUCT_IMAGES:
//                // 상품 이미지 저장 장소
//                uploadFileDir += "/productImages";
//                // 상품 이미지 정보 설정
//                product.setImageUrl(filename);
//                product.setImageUrlName(originalFilename);
//                break;
//            case PRODUCT_INFORMATION:
//                // 속성 이미지 저장 장소
//                uploadFileDir += "/productInformation";
//                // 속성 이미지 정보 설정
//                product.setProductInformationImage(filename);
//                break;
//            case PRODUCT_DESCRIPTION:
//                // 상세 이미지 저장 장소
//                uploadFileDir += "/productDescription";
//                // 상세 이미지 정보 설정
//                product.setProductDescriptionImage(filename);
//                break;
//            default:
//                throw new IllegalStateException("Unexpected value: " + idx);
//        }
//
//        // 파일을 저장할 디렉토리 취득
//        Path path = Paths.get(uploadFileDir, filename);
//
//        // 파일을 저장할 디렉토리가 없으면 생성 후 저장
//        try {
//            Files.createDirectories(path.getParent());
//            Files.write(path, file.getBytes());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public boolean updateCount(List<KakaoPay.ProductInfo> productInfo) {
//        boolean result = false;
//        List<Integer> row = new ArrayList<>();
//        productInfo.forEach(info
//                -> row.add(productRepository.decreaseCount(info.getPid(), info.getQty())));
//
//        if(!row.isEmpty()){
//            result = true;
//        }
//        return result;
//    }
//}


package com.tjg_project.candy.domain.product.service;

import com.tjg_project.candy.domain.order.entity.KakaoPay;
import com.tjg_project.candy.domain.order.repository.OrderDetailRepository;
import com.tjg_project.candy.domain.product.entity.Product;
import com.tjg_project.candy.domain.product.entity.ProductDetailView;
import com.tjg_project.candy.domain.product.entity.ProductQnA;
import com.tjg_project.candy.domain.product.repository.ProductDetailViewRepository;
import com.tjg_project.candy.domain.product.repository.ProductQnARepository;
import com.tjg_project.candy.domain.product.repository.ProductRepository;
import com.tjg_project.candy.domain.product.repository.ProductReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    // 이미지 종류 구분
    private final static int PRODUCT_IMAGES = 0;
    private final static int PRODUCT_INFORMATION = 1;
    private final static int PRODUCT_DESCRIPTION = 2;

    // Supabase 정보 (yml에서 읽어옴)
    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String bucketName;


    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductReviewRepository productReviewRepository;
    @Autowired
    private ProductQnARepository productQnARepository;
    @Autowired
    private ProductDetailViewRepository productDetailViewRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public List<Product> getProductList() {
        return productRepository.findAll();
    }

    @Override
    public List<Map<String, Object>> getProductReviewList() {
        return productReviewRepository.findAllReviewWithUserName();
    }

    @Override
    public List<Map<String, Object>> getProductProductQnAList() {
        return productQnARepository.findAllProductQnAWithUserName();
    }

    @Override
    public List<Product> getProductProductBestList() {
        return orderDetailRepository.findBestSellingProducts();
    }

    @Override
    public Optional<ProductDetailView> getProductDetail(Long id) {
        return productDetailViewRepository.findById(id);
    }


    @Override
    public Product saveProduct(Product product, List<MultipartFile> files) {

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            setImages(product, file, i);
        }

        product.setHotDeal(product.getDc() != 0);
        product.setProductDate(LocalDate.now());

        return productRepository.save(product);
    }


    @Override
    public Product updateProduct(Product product, List<MultipartFile> files) {

        Product findProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new RuntimeException("상품 없음"));

        findProduct.setAllergyInfo(product.getAllergyInfo());
        findProduct.setBrandName(product.getBrandName());
        findProduct.setCount(product.getCount());
        findProduct.setDc(product.getDc());
        findProduct.setDelType(product.getDelType());
        findProduct.setDescription(product.getDescription());
        findProduct.setNotes(product.getNotes());
        findProduct.setOrigin(product.getOrigin());
        findProduct.setPrice(product.getPrice());
        findProduct.setProductName(product.getProductName());
        findProduct.setSeller(product.getSeller());
        findProduct.setUnit(product.getUnit());
        findProduct.setWeight(product.getWeight());
        findProduct.setCategorySub(product.getCategorySub());

        for (int idx = 0; idx < files.size(); idx++) {
            MultipartFile file = files.get(idx);
            setImages(findProduct, file, idx);
        }

        findProduct.setHotDeal(product.getDc() != 0);
        findProduct.setProductDate(LocalDate.now());

        return productRepository.save(findProduct);
    }


    @Override
    public boolean deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }


    @Override
    public ProductQnA addProductQnA(ProductQnA qna) {
        qna.setStatus("답변대기");
        return productQnARepository.save(qna);
    }


    // -----------------------------
    //    🔥 Supabase 파일 업로드
    // -----------------------------
    private String uploadToSupabase(MultipartFile file, String directory) {

        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // Supabase 저장 경로
            String path = directory + "/" + fileName;

            // 업로드 URL
            String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + path;

            // Header
            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", supabaseKey);
            headers.set("Authorization", "Bearer " + supabaseKey);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            // Request entity
            HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> res = restTemplate.exchange(uploadUrl, HttpMethod.PUT, entity, String.class);

            if (res.getStatusCode().is2xxSuccessful()) {
                // public URL 반환
                return supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + path;
            }

            throw new RuntimeException("Supabase upload failed");

        } catch (Exception e) {
            throw new RuntimeException("Supabase Upload Error: " + e.getMessage());
        }
    }


    // ----------------------------------
    //   🔥 기존 setImages → Supabase 적용
    // ----------------------------------
    public void setImages(Product product, MultipartFile file, int idx) {

        if (file == null || file.isEmpty()) {
            return; // 파일 없으면 처리 X
        }

        String folder;
        String imageUrl;

        switch (idx) {
            case PRODUCT_IMAGES:
                folder = "productImages";
                imageUrl = uploadToSupabase(file, folder);
                product.setImageUrl(imageUrl);
                product.setImageUrlName(file.getOriginalFilename());
                break;

            case PRODUCT_INFORMATION:
                folder = "productInformation";
                imageUrl = uploadToSupabase(file, folder);
                product.setProductInformationImage(imageUrl);
                break;

            case PRODUCT_DESCRIPTION:
                folder = "productDescription";
                imageUrl = uploadToSupabase(file, folder);
                product.setProductDescriptionImage(imageUrl);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + idx);
        }
    }


    @Override
    public boolean updateCount(List<KakaoPay.ProductInfo> productInfo) {

        boolean result = false;
        List<Integer> row = new ArrayList<>();

        productInfo.forEach(info ->
                row.add(productRepository.decreaseCount(info.getPid(), info.getQty()))
        );

        if (!row.isEmpty()) {
            result = true;
        }
        return result;
    }
}
