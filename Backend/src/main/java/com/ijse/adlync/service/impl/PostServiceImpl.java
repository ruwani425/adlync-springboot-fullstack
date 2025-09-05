package com.ijse.adlync.service.impl;

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
import com.ijse.adlync.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private PostRepository repository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private Advertisement_typeRepository advertisement_typeRepository;
    private LocationRepository locationRepository;
    private AnimalRepository animalRepository;
    private VehicleRepository vehicleRepository;
    private PropertyRepository propertyRepository;

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

        postEntity.setPrice(dto.getPrice());
        postEntity.setStatus(dto.getStatus());

        return postEntity;
    }

    @Transactional
    public String createAnimalPost(AnimalRequestDTO requestDTO, String username) {
        PostEntity postEntity = buildBasePost(
                requestDTO.getPostRequestDTO(),
                username,
                CategoryEntityNameEnum.ANIMAL,
                Advertisement_typeEntityTypeEnum.SELL
        );
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

        VehicleEntity savedVehicle = vehicleRepository.save(vehicleEntity);
        savedPost.setVehicle(savedVehicle);
        repository.save(savedPost);

        return "save success";
    }

    @Transactional
    public String createPropertyPost(PropertyRequestDTO requestDTO, String username) {
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

        PropertyEntity savedProperty = propertyRepository.save(propertyEntity);
        savedPost.setProperty(savedProperty);
        repository.save(savedPost);

        return "saved success";
    }
}
