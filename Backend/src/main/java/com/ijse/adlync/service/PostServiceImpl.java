package com.ijse.adlync.service;

import com.ijse.adlync.dto.request.AnimalRequestDTO;
import com.ijse.adlync.dto.request.PostRequestDTO;
import com.ijse.adlync.dto.request.PropertyRequestDTO;
import com.ijse.adlync.dto.request.VehicleRequestDTO;
import com.ijse.adlync.dto.response.ImageResponseDTO;
import com.ijse.adlync.dto.response.PostResponseDTO;
import com.ijse.adlync.entity.*;
import com.ijse.adlync.entity.enums.Advertisement_typeEntityTypeEnum;
import com.ijse.adlync.entity.enums.CategoryEntityNameEnum;
import com.ijse.adlync.repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl {

    @Autowired
    private PostRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private Advertisement_typeRepository advertisement_typeRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<PostResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PostResponseDTO findById(Long id) {
        PostEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PostEntity not found with id: " + id));
        return toResponseDTO(entity);
    }

    public PostResponseDTO create(PostRequestDTO requestDTO) {
        PostEntity entity = toEntity(requestDTO);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public PostResponseDTO update(Long id, PostRequestDTO requestDTO) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("PostEntity not found with id: " + id);
        }
        PostEntity entity = toEntity(requestDTO);
        entity.setPost_id(id);
        entity = repository.save(entity);
        return toResponseDTO(entity);
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("PostEntity not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private PostResponseDTO toResponseDTO(PostEntity entity) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setPost_id(entity.getPost_id());
        dto.setStatus(entity.getStatus());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContact_number(entity.getContact_number());
        dto.setPrice(entity.getPrice());

        // Add image mapping
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

    @Transactional
    public String createAnimalPost(AnimalRequestDTO requestDTO, String username) {
        System.out.println(username);

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        PostEntity postEntity = new PostEntity();
        postEntity.setDescription(requestDTO.getPostRequestDTO().getDescription());
        postEntity.setTitle(requestDTO.getPostRequestDTO().getTitle());
        postEntity.setCategory(categoryRepository.findByName(CategoryEntityNameEnum.ANIMAL));
        postEntity.setAdvertisement_type(advertisement_typeRepository.findByType(Advertisement_typeEntityTypeEnum.SELL));
        postEntity.setUser(userEntity);
        postEntity.setContact_number(requestDTO.getPostRequestDTO().getContact_number());
        System.out.println(requestDTO.getPostRequestDTO().getContact_number() + "+  contact");
        List<ImageEntity> images = requestDTO.getPostRequestDTO().getImages().stream()
                .map(imgDto -> {
                    ImageEntity image = modelMapper.map(imgDto, ImageEntity.class);
                    image.setPost(postEntity);
                    return image;
                })
                .collect(Collectors.toList());
        postEntity.setImages(images);

        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setAddress(requestDTO.getPostRequestDTO().getAddress());
        locationEntity.setCity(requestDTO.getPostRequestDTO().getCity());
        locationEntity.setDistrict(requestDTO.getPostRequestDTO().getDistrict());
        postEntity.setLocation(locationEntity);

        postEntity.setPrice(requestDTO.getPostRequestDTO().getPrice());
        postEntity.setStatus(requestDTO.getPostRequestDTO().getStatus());

        PostEntity savedPost = repository.save(postEntity);

        AnimalEntity animalEntity = new AnimalEntity();
        animalEntity.setAge(requestDTO.getAge());
        animalEntity.setGender(requestDTO.getGender());
        animalEntity.setSpecies(requestDTO.getSpecies());
        animalEntity.setBreed(requestDTO.getBreed());
        animalEntity.setVaccination_status(requestDTO.getVaccination_status());
        animalEntity.setPost(savedPost);

        AnimalEntity savedAnimal = animalRepository.save(animalEntity);

        savedPost.setAnimal(savedAnimal);
        repository.save(savedPost);

        return "save success";
    }

    @Transactional
    public String createVehiclePost(VehicleRequestDTO requestDTO, String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        PostEntity postEntity = new PostEntity();
        postEntity.setDescription(requestDTO.getPostRequestDTO().getDescription());
        postEntity.setTitle(requestDTO.getPostRequestDTO().getTitle());
        postEntity.setCategory(categoryRepository.findByName(CategoryEntityNameEnum.VEHICLE));
        postEntity.setAdvertisement_type(advertisement_typeRepository.findByType(requestDTO.getAdvertisementType()));
        postEntity.setUser(userEntity);
        postEntity.setContact_number(requestDTO.getPostRequestDTO().getContact_number());

        List<ImageEntity> images = requestDTO.getPostRequestDTO().getImages().stream()
                .map(imgDto -> {
                    ImageEntity image = modelMapper.map(imgDto, ImageEntity.class);
                    image.setPost(postEntity);
                    return image;
                })
                .collect(Collectors.toList());
        postEntity.setImages(images);

        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setAddress(requestDTO.getPostRequestDTO().getAddress());
        locationEntity.setCity(requestDTO.getPostRequestDTO().getCity());
        locationEntity.setDistrict(requestDTO.getPostRequestDTO().getDistrict());
        postEntity.setLocation(locationEntity);

        postEntity.setPrice(requestDTO.getPostRequestDTO().getPrice());
        postEntity.setStatus(requestDTO.getPostRequestDTO().getStatus());

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

        VehicleEntity savedVehicle = vehicleRepository.save(vehicleEntity);
        savedPost.setVehicle(savedVehicle);
        repository.save(savedPost);
        return "save success";
    }

    public String createPropertyPost(PropertyRequestDTO requestDTO, String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        PostEntity postEntity = new PostEntity();
        postEntity.setDescription(requestDTO.getPostRequestDTO().getDescription());
        postEntity.setTitle(requestDTO.getPostRequestDTO().getTitle());
        postEntity.setCategory(categoryRepository.findByName(CategoryEntityNameEnum.VEHICLE));
        postEntity.setAdvertisement_type(advertisement_typeRepository.findByType(requestDTO.getAdvertisement_type()));
        postEntity.setUser(userEntity);
        postEntity.setContact_number(requestDTO.getPostRequestDTO().getContact_number());
        List<ImageEntity> images = requestDTO.getPostRequestDTO().getImages().stream()
                .map(imgDto -> {
                    ImageEntity image = modelMapper.map(imgDto, ImageEntity.class);
                    image.setPost(postEntity);
                    return image;
                })
                .collect(Collectors.toList());
        postEntity.setImages(images);

        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setAddress(requestDTO.getPostRequestDTO().getAddress());
        locationEntity.setCity(requestDTO.getPostRequestDTO().getCity());
        locationEntity.setDistrict(requestDTO.getPostRequestDTO().getDistrict());
        postEntity.setLocation(locationEntity);

        postEntity.setPrice(requestDTO.getPostRequestDTO().getPrice());
        postEntity.setStatus(requestDTO.getPostRequestDTO().getStatus());

        PostEntity savedPost = repository.save(postEntity);
        PropertyEntity propertyEntity = new PropertyEntity();


        return null;
    }
}
