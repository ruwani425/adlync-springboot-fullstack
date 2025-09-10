package com.ijse.adlync.service.impl;

import com.ijse.adlync.dto.request.*;
import com.ijse.adlync.dto.response.*;
import com.ijse.adlync.entity.*;
import com.ijse.adlync.entity.enums.Advertisement_typeEntityTypeEnum;
import com.ijse.adlync.entity.enums.CategoryEntityNameEnum;
import com.ijse.adlync.entity.enums.PostEntityStatusEnum;
import com.ijse.adlync.repository.*;
import com.ijse.adlync.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final Advertisement_typeRepository advertisement_typeRepository;
    private final AnimalRepository animalRepository;
    private final VehicleRepository vehicleRepository;
    private final PropertyRepository propertyRepository;
    private final AgricultureRepository agricultureRepository;
    private final EducationRepository educationRepository;
    private final ElectronicRepository electronicRepository;
    private final JobRepository jobRepository;
    private final KidsRepository kidsRepository;
    private final EntertaintmentRepository entertaintmentRepository;
    private final EssentialsRepository essentialsRepository;
    private final Fashion_and_beautyRepository fashion_and_beautyRepository;
    private final Home_and_gardenRepository home_and_gardenRepository;
    private final MobileRepository mobileRepository;
    private final ServicesRepository servicesRepository;
    private final SportRepository sportRepository;
    private final Work_over_seasRepository work_over_seasRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PostResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<PostResponseDTO> findAll(Pageable pageable) {
//        Page<PostEntity> postPage = repository.findAll(pageable);
//        Page<PostResponseDTO> dtoPage = postPage.map(this::toResponseDTO);
        Pageable page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<PostEntity> dtoPage = repository.findAll(page);
        List<PostResponseDTO> contentlist = new ArrayList<>();
        dtoPage.getContent().forEach(dto -> contentlist.add(toResponseDTO(dto)));

        System.out.println(dtoPage.getContent());
        System.out.println("DB total elements: " + dtoPage.getTotalElements());
        dtoPage.getContent().forEach(System.out::println);
        return new PageResponse<>(
                contentlist,
                dtoPage.getNumber(),
                dtoPage.getSize(),
                dtoPage.getTotalElements(),
                dtoPage.getTotalPages(),
                dtoPage.isLast()
        );
    }

    @Override
    public PageResponse<PostResponseDTO> findAllByStatus(PostEntityStatusEnum status, Pageable pageable) {
        Page<PostEntity> postPage = repository.findAllByStatus(status, pageable);

        List<PostResponseDTO> contentList = postPage
                .stream()
                .map(this::toResponseDTO)
                .toList();

        return new PageResponse<>(
                contentList,
                postPage.getNumber(),
                postPage.getSize(),
                postPage.getTotalElements(),
                postPage.getTotalPages(),
                postPage.isLast()
        );
    }

    @Override
    public PostResponseDTO approvePost(Long id) {
        PostEntity post = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        if (post.getStatus() == PostEntityStatusEnum.APPROVED) {
            throw new RuntimeException("Post is already approved");
        }

        post.setStatus(PostEntityStatusEnum.APPROVED);
        post = repository.save(post);

        return modelMapper.map(post, PostResponseDTO.class);
    }


    @Override
    public PostResponseDTO findById(Long id) {
        PostEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PostEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    @Override
    public PostResponseDTO create(PostRequestDTO requestDTO) {
        PostEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    @Override
    public PostResponseDTO update(Long id, PostRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("PostEntity not found with id: " + id);
        }
        PostEntity entity = toEntity(requestDTO);
        entity.setPost_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("PostEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Transactional
    @Override
    public PostResponseDTO createAnimalPost(AnimalRequestDTO requestDTO, String username) {
        PostEntity postEntity = buildBasePost(
                requestDTO.getPostRequestDTO(),
                username,
                CategoryEntityNameEnum.ANIMAL,
                Advertisement_typeEntityTypeEnum.SELL
        );
        System.out.println(requestDTO.getPostRequestDTO().toString());
        PostEntity savedPost = repository.save(postEntity);

        AnimalEntity animalEntity = new AnimalEntity();
        animalEntity.setAge(requestDTO.getAge());
        animalEntity.setGender(requestDTO.getGender());
        animalEntity.setSpecies(requestDTO.getSpecies());
        animalEntity.setBreed(requestDTO.getBreed());
        animalEntity.setVaccination_status(requestDTO.getVaccination_status());
        animalEntity.setPost(savedPost);

        savedPost.setAnimal(animalRepository.save(animalEntity));
        return toResponseDTO(repository.save(savedPost));
    }

    @Transactional
    @Override
    public PostResponseDTO createVehiclePost(VehicleRequestDTO requestDTO, String username) {
        PostEntity postEntity = buildBasePost(
                requestDTO.getPostRequestDTO(),
                username,
                CategoryEntityNameEnum.VEHICLE,
                requestDTO.getAdvertisementType()
        );
        PostEntity savedPost = repository.save(postEntity);

        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setBrand(requestDTO.getBrand());
        vehicleEntity.setModel(requestDTO.getModel());
        vehicleEntity.setVehicle_type(requestDTO.getVehicle_type());
        vehicleEntity.setCondition(requestDTO.getCondition());
        vehicleEntity.setFuel_type(requestDTO.getFuel_type());
        vehicleEntity.setMileage(requestDTO.getMileage());
        vehicleEntity.setTransmission(requestDTO.getTransmission());
        vehicleEntity.setYear(requestDTO.getYear());
        vehicleEntity.setPost(savedPost);

        savedPost.setVehicle(vehicleRepository.save(vehicleEntity));
        return toResponseDTO(repository.save(savedPost));
    }

    @Transactional
    @Override
    public PostResponseDTO createPropertyPost(PropertyRequestDTO requestDTO, String username) {
        PostEntity postEntity = buildBasePost(
                requestDTO.getPostRequestDTO(),
                username,
                CategoryEntityNameEnum.PROPERTY,
                requestDTO.getAdvertisement_type()
        );
        PostEntity savedPost = repository.save(postEntity);

        PropertyEntity propertyEntity = new PropertyEntity();
        propertyEntity.setBarthroom(requestDTO.getBarthroom());
        propertyEntity.setBedroom(requestDTO.getBedroom());
        propertyEntity.setType(requestDTO.getType());
        propertyEntity.setFurnished(requestDTO.getFurnished());
        propertyEntity.setLand_size(requestDTO.getLand_size());
        propertyEntity.setPost(savedPost);

        savedPost.setProperty(propertyRepository.save(propertyEntity));
        return toResponseDTO(repository.save(savedPost));
    }

    @Transactional
    @Override
    public PostResponseDTO createAgriculturePost(AgricultureRequestDTO requestDTO, String username) {
        PostEntity postEntity = buildBasePost(
                requestDTO.getPostRequestDTO(),
                username,
                CategoryEntityNameEnum.AGRICULTURE,
                Advertisement_typeEntityTypeEnum.SELL
        );
        PostEntity savedPost = repository.save(postEntity);

        AgricultureEntity agricultureEntity = new AgricultureEntity();
        agricultureEntity.setCondition(requestDTO.getCondition());
        agricultureEntity.setCertifications(requestDTO.getCertifications());
        agricultureEntity.setSeason(requestDTO.getSeason());
        agricultureEntity.setProduction_Date(requestDTO.getProduction_Date());
        agricultureEntity.setQuantity(requestDTO.getQuantity());
        agricultureEntity.setVariety(requestDTO.getVariety());
        agricultureEntity.setProduct_type(requestDTO.getProduct_type());
        agricultureEntity.setPost(savedPost);

        savedPost.setAgriculture(agricultureRepository.save(agricultureEntity));
        return toResponseDTO(repository.save(savedPost));
    }

    @Transactional
    @Override
    public PostResponseDTO createEducationPost(EducationRequestDTO requestDTO, String username) {
        PostEntity postEntity = buildBasePost(
                requestDTO.getPostRequestDTO(),
                username,
                CategoryEntityNameEnum.EDUCATION,
                Advertisement_typeEntityTypeEnum.SERVICE
        );
        PostEntity savedPost = repository.save(postEntity);

        EducationEntity educationEntity = new EducationEntity();
        educationEntity.setCourse_name(requestDTO.getCourse_name());
        educationEntity.setDuration(requestDTO.getDuration());
        educationEntity.setEducation_level(requestDTO.getEducation_level());
        educationEntity.setInstitute(requestDTO.getInstitute());
        educationEntity.setQualification_offered(requestDTO.getQualification_offered());
        educationEntity.setRequirements(requestDTO.getRequirements());
        educationEntity.setSchedule(requestDTO.getSchedule());
        educationEntity.setStudy_mode(requestDTO.getStudy_mode());
        educationEntity.setSubject_area(requestDTO.getSubject_area());
        educationEntity.setPost(savedPost);

        savedPost.setEducation(educationRepository.save(educationEntity));
        return toResponseDTO(repository.save(savedPost));
    }

    @Override
    public PostResponseDTO createElectronicPost(ElectronicRequestDTO requestDTO, String username) {
        PostEntity postEntity = buildBasePost(
                requestDTO.getPostRequestDTO(),
                username,
                CategoryEntityNameEnum.ELECTRONIC,
                requestDTO.getAdvertisementType()
        );
        PostEntity savedPost = repository.save(postEntity);
        ElectronicEntity electronicEntity = new ElectronicEntity();
        electronicEntity.setType(requestDTO.getType());
        System.out.println(requestDTO.getType());
        electronicEntity.setModel(requestDTO.getModel());
        electronicEntity.setBrand(requestDTO.getBrand());
        electronicEntity.setAccessories(requestDTO.getAccessories());
        electronicEntity.setWarranty(requestDTO.getWarranty());
        electronicEntity.setCondition(requestDTO.getCondition());
        electronicEntity.setPost(savedPost);

        savedPost.setElectronic(electronicRepository.save(electronicEntity));
        return toResponseDTO(repository.save(savedPost));
    }

    @Override
    public PostResponseDTO createEntertaintmentPost(EntertaintmentRequestDTO requestDTO, String username) {
        PostEntity postEntity = buildBasePost(
                requestDTO.getPostRequestDTO(),
                username,
                CategoryEntityNameEnum.ENTERTAINTMENT,
                requestDTO.getAdvertisementType()
        );
        PostEntity savedPost = repository.save(postEntity);
        EntertaintmentEntity entertaintmentEntity = new EntertaintmentEntity();
        entertaintmentEntity.setType(requestDTO.getType());
        entertaintmentEntity.setRelease_year(requestDTO.getRelease_year());
        entertaintmentEntity.setBrand(requestDTO.getBrand());
        entertaintmentEntity.setCreator(requestDTO.getCreator());
        entertaintmentEntity.setFormat(requestDTO.getFormat());
        entertaintmentEntity.setGenre(requestDTO.getGenre());
        entertaintmentEntity.setRating(requestDTO.getRating());
        entertaintmentEntity.setCondition(requestDTO.getCondition());
        entertaintmentEntity.setPost(savedPost);

        savedPost.setEntertainment(entertaintmentRepository.save(entertaintmentEntity));
        return toResponseDTO(repository.save(savedPost));
    }

    @Override
    public PostResponseDTO createEssentialPost(EssentialsRequestDTO requestDTO, String username) {
        PostEntity postEntity = buildBasePost(
                requestDTO.getPostRequestDTO(),
                username,
                CategoryEntityNameEnum.ESSENTIALS,
                requestDTO.getAdvertisementType()
        );
        PostEntity savedPost = repository.save(postEntity);
        EssentialsEntity essentialsEntity = new EssentialsEntity();
        essentialsEntity.setQuantity(requestDTO.getQuantity());
        essentialsEntity.setExpiry_date(requestDTO.getExpiry_date());
        essentialsEntity.setBrand(requestDTO.getBrand());
        essentialsEntity.setProduct_type(requestDTO.getProduct_type());
        essentialsEntity.setStorage_instructions(requestDTO.getStorage_instructions());
        essentialsEntity.setCondition(requestDTO.getCondition());
        essentialsEntity.setPost(savedPost);

        savedPost.setEssentials(essentialsRepository.save(essentialsEntity));
        return toResponseDTO(repository.save(savedPost));
    }

    @Override
    public PostResponseDTO createFashionAndBeautyPost(Fashion_and_beautyRequestDTO requestDTO, String username) {
        PostEntity postEntity = buildBasePost(
                requestDTO.getPostRequestDTO(),
                username,
                CategoryEntityNameEnum.FASHION_AND_BEAUTY,
                requestDTO.getAdvertisementType()
        );
        PostEntity savedPost = repository.save(postEntity);
        Fashion_and_beautyEntity beautyEntity = new Fashion_and_beautyEntity();
        beautyEntity.setBrand(requestDTO.getBrand());
        beautyEntity.setColor(requestDTO.getColor());
        beautyEntity.setCondition(requestDTO.getCondition());
        beautyEntity.setItem_type(requestDTO.getItem_type());
        beautyEntity.setMaterial(requestDTO.getMaterial());
        beautyEntity.setSize(requestDTO.getSize());
        beautyEntity.setStyle_note(requestDTO.getStyle_note());
        beautyEntity.setGender(requestDTO.getGender());
        beautyEntity.setPost(savedPost);

        savedPost.setFashion_and_beauty(fashion_and_beautyRepository.save(beautyEntity));
        return toResponseDTO(repository.save(savedPost));
    }

    @Override
    public PostResponseDTO createHomeAndGardenPost(Home_and_gardenRequestDTO requestDTO, String username) {
        PostEntity postEntity = buildBasePost(
                requestDTO.getPostRequestDTO(),
                username,
                CategoryEntityNameEnum.HOME_AND_GARDEN,
                requestDTO.getAdvertisementType()
        );
        System.out.println(requestDTO.getAdvertisementType());
        PostEntity savedPost = repository.save(postEntity);
        Home_and_gardenEntity homeEntity = new Home_and_gardenEntity();
        homeEntity.setAssembly_required(requestDTO.getAssembly_required());
        homeEntity.setBrand(requestDTO.getBrand());
        homeEntity.setColor(requestDTO.getColor());
        homeEntity.setCondition(requestDTO.getCondition());
        homeEntity.setDimensions(requestDTO.getDimensions());
        homeEntity.setItem_type(requestDTO.getItem_type());
        homeEntity.setMaterial(requestDTO.getMaterial());
        homeEntity.setSpecial_features(requestDTO.getSpecial_features());
        homeEntity.setWeight(requestDTO.getWeight());
        homeEntity.setPost(savedPost);

        savedPost.setHome_and_garden(home_and_gardenRepository.save(homeEntity));
        return toResponseDTO(repository.save(savedPost));
    }

    @Override
    public PostResponseDTO createJobPost(JobRequestDTO requestDTO, String username) {
        PostEntity postEntity = buildBasePost(
                requestDTO.getPostRequestDTO(),
                username,
                CategoryEntityNameEnum.JOB,
                Advertisement_typeEntityTypeEnum.JOB
        );
        PostEntity savedPost = repository.save(postEntity);
        JobEntity jobEntity = new JobEntity();
        jobEntity.setSalary_max(requestDTO.getSalary_max());
        jobEntity.setSalary_min(requestDTO.getSalary_min());
        jobEntity.setCompany(requestDTO.getCompany());
        jobEntity.setExpiriance_level(requestDTO.getExpiriance_level());
        jobEntity.setIndustry(requestDTO.getIndustry());
        jobEntity.setPosition(requestDTO.getPosition());
        jobEntity.setRequirements(requestDTO.getRequirements());
        jobEntity.setJob_type(requestDTO.getJob_type());
        jobEntity.setPost(savedPost);

        savedPost.setJob(jobRepository.save(jobEntity));
        return toResponseDTO(repository.save(savedPost));
    }

    @Override
    public PostResponseDTO createKidsPost(KidsRequestDTO requestDTO, String username) {
        PostEntity postEntity = buildBasePost(
                requestDTO.getPostRequestDTO(),
                username,
                CategoryEntityNameEnum.KIDS,
                Advertisement_typeEntityTypeEnum.SELL
        );
        PostEntity savedPost = repository.save(postEntity);
        KidsEntity kidsEntity = new KidsEntity();
        kidsEntity.setGender(requestDTO.getGender());
        kidsEntity.setAge_range(requestDTO.getAge_range());
        kidsEntity.setBrand(requestDTO.getBrand());
        kidsEntity.setCondition(requestDTO.getCondition());
        kidsEntity.setItem_type(requestDTO.getItem_type());
        kidsEntity.setSafety_information(requestDTO.getSafety_information());
        kidsEntity.setSize(requestDTO.getSize());
        kidsEntity.setPost(savedPost);

        savedPost.setKids(kidsRepository.save(kidsEntity));
        return toResponseDTO(repository.save(savedPost));
    }

    @Override
    public PostResponseDTO createMobilePost(MobileRequestDTO requestDTO, String username) {
        PostEntity postEntity = buildBasePost(
                requestDTO.getPostRequestDTO(),
                username,
                CategoryEntityNameEnum.MOBILE,
                requestDTO.getAdvertisementType()
        );
        PostEntity savedPost = repository.save(postEntity);
        MobileEntity mobileEntity = new MobileEntity();
        mobileEntity.setBrand(requestDTO.getBrand());
        mobileEntity.setAdditional_information(requestDTO.getAdditional_information());
        mobileEntity.setColour(requestDTO.getColour());
        mobileEntity.setCondition(requestDTO.getCondition());
        mobileEntity.setIncluded_accessories(requestDTO.getIncluded_accessories());
        mobileEntity.setModel(requestDTO.getModel());
        mobileEntity.setRam(requestDTO.getRam());
        mobileEntity.setStorage(requestDTO.getStorage());
        mobileEntity.setWarranty_status(requestDTO.getWarranty_status());
        mobileEntity.setPost(savedPost);

        savedPost.setMobile(mobileRepository.save(mobileEntity));
        return toResponseDTO(repository.save(savedPost));
    }

    @Override
    public PostResponseDTO createServicePost(ServicesRequestDTO requestDTO, String username) {
        PostEntity postEntity = buildBasePost(
                requestDTO.getPostRequestDTO(),
                username,
                CategoryEntityNameEnum.SERVICES,
                Advertisement_typeEntityTypeEnum.SERVICE
        );
        PostEntity savedPost = repository.save(postEntity);
        ServicesEntity servicesEntity = new ServicesEntity();
        servicesEntity.setCharges(requestDTO.getCharges());
        servicesEntity.setProvider_name(requestDTO.getProvider_name());
        servicesEntity.setQualifications(requestDTO.getQualifications());
        servicesEntity.setService_area(requestDTO.getService_area());
        servicesEntity.setService_type(requestDTO.getService_type());
        servicesEntity.setAvailability(requestDTO.getAvailability());
        servicesEntity.setPost(savedPost);

        savedPost.setServices(servicesRepository.save(servicesEntity));
        return toResponseDTO(repository.save(savedPost));
    }

    @Override
    public PostResponseDTO createSportPost(SportRequestDTO requestDTO, String username) {
        PostEntity postEntity = buildBasePost(
                requestDTO.getPostRequestDTO(),
                username,
                CategoryEntityNameEnum.SPORT,
                requestDTO.getAdvertisementType()
        );
        System.out.println(requestDTO.getAdvertisementType());
        PostEntity savedPost = repository.save(postEntity);
        SportEntity sportEntity = new SportEntity();
        sportEntity.setAdditional_information(requestDTO.getAdditional_information());
        sportEntity.setBrand(requestDTO.getBrand());
        sportEntity.setCondition(requestDTO.getCondition());
        sportEntity.setEquipment_type(requestDTO.getEquipment_type());
        sportEntity.setSize(requestDTO.getSize());
        sportEntity.setPost(savedPost);

        savedPost.setSport(sportRepository.save(sportEntity));
        return toResponseDTO(repository.save(savedPost));
    }

    @Override
    public PostResponseDTO createWorkOverSeaPost(Work_over_seasRequestDTO requestDTO, String username) {
        PostEntity postEntity = buildBasePost(
                requestDTO.getPostRequestDTO(),
                username,
                CategoryEntityNameEnum.WORK_OVERSEAS,
                Advertisement_typeEntityTypeEnum.JOB
        );
        PostEntity savedPost = repository.save(postEntity);
        Work_over_seasEntity work_over_seasEntity = new Work_over_seasEntity();
        work_over_seasEntity.setAccommodation(requestDTO.getAccommodation());
        work_over_seasEntity.setAdditional_benefits(requestDTO.getAdditional_benefits());
        work_over_seasEntity.setCompany_or_agency_name(requestDTO.getCompany_name());
        work_over_seasEntity.setContract_duration(requestDTO.getContract_duration());
        work_over_seasEntity.setCountry(requestDTO.getCountry());
        work_over_seasEntity.setPosition(requestDTO.getPosition());
        work_over_seasEntity.setRequirements(requestDTO.getRequirements());
        work_over_seasEntity.setSalary(requestDTO.getSalary());
        work_over_seasEntity.setVisa_status(requestDTO.getVisa_status());
        work_over_seasEntity.setPost(savedPost);

        savedPost.setWork_over_seas(work_over_seasRepository.save(work_over_seasEntity));
        return toResponseDTO(repository.save(savedPost));
    }


    private PostResponseDTO toResponseDTO(PostEntity entity) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setPost_id(entity.getPost_id());
        dto.setStatus(entity.getStatus());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContact_number(entity.getContact_number());
        dto.setPrice(entity.getPrice());
        dto.setCreatedAt(entity.getCreatedAt());

        if (entity.getUser() != null) {
            dto.setUser(modelMapper.map(entity.getUser(), UserResponseDTO.class));
        }

        if (entity.getCategory() != null) {
            dto.setCategory(modelMapper.map(entity.getCategory(), CategoryResponseDTO.class));
        }

        if (entity.getLocation() != null) {
            dto.setLocation(modelMapper.map(entity.getLocation(), LocationResponseDTO.class));
        }

        if (entity.getPayment() != null) {
            dto.setPayment(modelMapper.map(entity.getPayment(), PaymentResponseDTO.class));
            System.out.println("\n\n\n\n" + entity.getPayment());
        }

        if (entity.getImages() != null) {
            List<ImageResponseDTO> imageDTOs = entity.getImages().stream()
                    .map(this::toImageResponseDTO)
                    .collect(Collectors.toList());
            dto.setImages(imageDTOs);
        }
        return dto;
    }


    private ImageResponseDTO toImageResponseDTO(ImageEntity entity) {
        ImageResponseDTO dto = new ImageResponseDTO();
        dto.setImage_id(entity.getImage_id());
        dto.setImage_url(entity.getImage_url());
        dto.setPost_id(entity.getPost().getPost_id());
        return dto;
    }

    private PostEntity toEntity(PostRequestDTO dto) {
        PostEntity entity = new PostEntity();
        entity.setStatus(dto.getStatus());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    private PostEntity buildBasePost(PostRequestDTO dto, String username,
                                     CategoryEntityNameEnum categoryEnum,
                                     Advertisement_typeEntityTypeEnum adTypeEnum) {

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        PostEntity postEntity = new PostEntity();
        postEntity.setDescription(dto.getDescription());
        postEntity.setTitle(dto.getTitle());
        postEntity.setCategory(categoryRepository.findByName(categoryEnum));
        postEntity.setAdvertisement_type(advertisement_typeRepository.findByType(adTypeEnum));
        postEntity.setUser(userEntity);
        postEntity.setContact_number(dto.getContact_number());
        postEntity.setPrice(dto.getPrice());
        postEntity.setStatus(dto.getStatus());
        List<ImageEntity> images = dto.getImages().stream()
                .map(imgDto -> {
                    ImageEntity image = modelMapper.map(imgDto, ImageEntity.class);
                    image.setPost(postEntity);
                    return image;
                })
                .collect(Collectors.toList());
        postEntity.setImages(images);

        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setAddress(dto.getAddress());
        locationEntity.setCity(dto.getCity());
        locationEntity.setDistrict(dto.getDistrict());
        postEntity.setLocation(locationEntity);

        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setAmount(dto.getPrice());
        paymentEntity.setPayment_date(dto.getPayment_date());
        paymentEntity.setPayment_type(dto.getPayment_type());
        paymentEntity.setStatus(dto.getPayment_status());
        paymentEntity.setSlip_url(dto.getSlip_url());
        paymentEntity.setPost(postEntity);
        postEntity.setPayment(paymentEntity);
        return postEntity;
    }
}
