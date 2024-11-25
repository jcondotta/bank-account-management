package com.jcondotta.service;

import com.jcondotta.event.AccountHolderCreatedNotification;
import com.jcondotta.exception.SerializationException;
import com.jcondotta.helper.TestAccountHolderRequest;
import com.jcondotta.helper.TestBankAccountId;
import io.micronaut.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class SerializationServiceTest {

    private static final UUID BANK_ACCOUNT_ID_BRAZIL = TestBankAccountId.BRAZIL.getBankAccountId();
    private static final String ACCOUNT_HOLDER_NAME_JEFFERSON = TestAccountHolderRequest.JEFFERSON.getAccountHolderName();

    @InjectMocks
    private SerializationService serializationService;

    private JsonMapper jsonMapper = JsonMapper.createDefault();

    @BeforeEach
    void beforeEach() {
        serializationService = new SerializationService(jsonMapper);
    }

    @Test
    void shouldSerializeNotificationToJson_whenNotificationObjectIsValid() {
        var notification = new AccountHolderCreatedNotification(UUID.randomUUID(), ACCOUNT_HOLDER_NAME_JEFFERSON, BANK_ACCOUNT_ID_BRAZIL);

        String json = serializationService.serialize(notification);

        assertThat(json).contains("accountHolderId", "accountHolderName", "bankAccountId");
    }

    @Test
    void shouldDeserializeJsonToNotification_whenJSONIsValid() {
        String json = """
                {
                    "accountHolderId": "123e4567-e89b-12d3-a456-426614174000",
                    "accountHolderName": "Jefferson Condotta",
                    "bankAccountId": "01920bff-1338-7efd-ade6-e9128debe5d4"
                }
                """;

        var notification = serializationService.deserialize(json, AccountHolderCreatedNotification.class);

        assertThat(notification.accountHolderId()).isEqualTo(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        assertThat(notification.accountHolderName()).isEqualTo(ACCOUNT_HOLDER_NAME_JEFFERSON);
        assertThat(notification.bankAccountId()).isEqualTo(BANK_ACCOUNT_ID_BRAZIL);
    }

    @Test
    void shouldThrowSerializationException_whenJsonIsMalformed() {
        String invalidJson = "{ invalid }";

        assertThrows(SerializationException.class,
                () -> serializationService.deserialize(invalidJson, AccountHolderCreatedNotification.class)
        );
    }

    @Test
    void shouldThrowSerializationException_whenFieldTypeMismatchOccurs() {
        String invalidJson = """
                {
                    "accountHolderId": 12345,
                    "accountHolderName": "Jefferson Condotta",
                    "bankAccountId": "01920bff-1338-7efd-ade6-e9128debe5d4"
                }
                """;

        assertThrows(SerializationException.class,
                () -> serializationService.deserialize(invalidJson, AccountHolderCreatedNotification.class)
        );
    }

    @Test
    void shouldHandleNullValues() {
        var notification = new AccountHolderCreatedNotification(null, null, null);

        String json = serializationService.serialize(notification);
        var deserializedNotification = serializationService.deserialize(json, AccountHolderCreatedNotification.class);

        assertThat(deserializedNotification.accountHolderId()).isNull();
        assertThat(deserializedNotification.accountHolderName()).isNull();
        assertThat(deserializedNotification.bankAccountId()).isNull();
    }

}
