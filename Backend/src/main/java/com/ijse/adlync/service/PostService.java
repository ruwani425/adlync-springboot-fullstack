package com.ijse.adlync.service;

import com.ijse.adlync.dto.request.*;
import com.ijse.adlync.dto.response.PostResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    PostResponseDTO findById(Long id);

    PostResponseDTO create(PostRequestDTO requestDTO);

    PostResponseDTO update(Long id, PostRequestDTO requestDTO);

    void deleteById(Long id);

    PostResponseDTO createAnimalPost(AnimalRequestDTO requestDTO, String username);

    PostResponseDTO createVehiclePost(VehicleRequestDTO requestDTO, String username);

    PostResponseDTO createPropertyPost(PropertyRequestDTO requestDTO, String username);

    PostResponseDTO createAgriculturePost(AgricultureRequestDTO requestDTO, String username);

    PostResponseDTO createEducationPost(EducationRequestDTO requestDTO, String username);

    PostResponseDTO createElectronicPost(ElectronicRequestDTO requestDTO, String username);

    PostResponseDTO createEntertaintmentPost(EntertaintmentRequestDTO requestDTO, String username);

    PostResponseDTO createEssentialPost(EssentialsRequestDTO requestDTO, String username);

    PostResponseDTO createFashionAndBeautyPost(Fashion_and_beautyRequestDTO requestDTO, String username);

    PostResponseDTO createHomeAndGardenPost(Home_and_gardenRequestDTO requestDTO, String username);

    PostResponseDTO createJobPost(JobRequestDTO requestDTO, String username);

    PostResponseDTO createKidsPost(KidsRequestDTO requestDTO, String username);

    PostResponseDTO createMobilePost(MobileRequestDTO requestDTO, String username);

    PostResponseDTO createServicePost(ServicesRequestDTO requestDTO, String username);

    PostResponseDTO createSportPost(SportRequestDTO requestDTO, String username);

    PostResponseDTO createWorkOverSeaPost(Work_over_seasRequestDTO requestDTO, String username);

    List<PostResponseDTO> findAll();

    Page<PostResponseDTO> findAll(Pageable pageable);
}
