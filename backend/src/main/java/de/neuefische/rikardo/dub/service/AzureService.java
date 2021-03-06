package de.neuefische.rikardo.dub.service;

import com.amazonaws.util.IOUtils;
import de.neuefische.rikardo.dub.model.azure.AzureResult;
import de.neuefische.rikardo.dub.model.azure.IdentifiedProfile;
import de.neuefische.rikardo.dub.model.voiceactor.VoiceActor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AzureService {

    @Value("${ocp.apim.subscription.key:defaultApiKeyPlaceholder}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private final DbService dbService;

    public AzureService(DbService dbService) {
        this.dbService = dbService;
    }

    public IdentifiedProfile requestAzureRestApi(MultipartFile file) throws IOException {

        try {

            String url = "https://westus.api.cognitive.microsoft.com" +
                    "/speaker/identification/v2.0/text-independent/profiles/identifySingleSpeaker" +
                    "?profileIds=" + getAllVoiceActorIds();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Ocp-Apim-Subscription-Key",apiKey);

            HttpEntity<byte[]> requestEntity = new HttpEntity<>(IOUtils.toByteArray(file.getInputStream()),headers);

            ResponseEntity<AzureResult> response = restTemplate.exchange(url,
                    HttpMethod.POST, requestEntity, AzureResult.class);

            System.out.println(response.getBody());

            return response.getBody().getIdentifiedProfile();

        } catch (Exception e) {
            return new IdentifiedProfile("00000000-0000-0000-0000-000000000000",0.0);
        }

    }

    public String identifySpeaker(MultipartFile file) throws IOException {

        IdentifiedProfile identifiedProfile = requestAzureRestApi(file);

        if (identifiedProfile.getProfileId().equals("00000000-0000-0000-0000-000000000000")) {
            System.out.println("Couldn't identify speaker");
            return "couldntIdentifySpeaker";
        } else {
            VoiceActor voiceActor = dbService.getVoiceActorById(identifiedProfile.getProfileId());
            return voiceActor.getId();
        }

    }

    public String getAllVoiceActorIds() {

        List<VoiceActor> voiceActorList = dbService.getAllVoiceActors();

        StringBuilder idString = new StringBuilder();
        for(VoiceActor voiceActor: voiceActorList) {
            idString.append(voiceActor.getId()).append(",");
        }

        return idString.toString();
    }

}
