package io.github.afikur;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class PhoneBookServiceTest {
    @Mock
    PhoneBookRepository phoneBookRepository;

    @InjectMocks
    PhoneBookService phoneBookService;

    String contactName = "Jerin";
    String tooLongPhoneNumber = "01111111111111";

    @Test
    public void givenValidContactName_whenSearch_thenReturnPhoneNumber() {
        given(phoneBookRepository.contains(contactName)).willReturn(true);
        given(phoneBookRepository.getPhoneNumberByContactName(contactName)).willReturn("012345");

        String phoneNumber = phoneBookService.search(contactName);

        then(phoneBookRepository).should().contains(contactName);
        then(phoneBookRepository).should().getPhoneNumberByContactName(contactName);
        assertEquals("012345", phoneNumber);
    }

    @Test
    public void givenValidContactNameAndPhoneNumber_whenRegister_thenSucceed() {
        given(phoneBookRepository.contains(contactName)).willReturn(false);

        phoneBookService.register(contactName, "012345");

        then(phoneBookRepository).should().insert(contactName, "012345");
    }


    @Test
    public void givenInvalidContactName_whenSearch_thenReturnNull() {
        given(phoneBookRepository.contains("x")).willReturn(false);

        String phoneNumber = phoneBookService.search("x");

        then(phoneBookRepository).should().contains("x");
        then(phoneBookRepository).should(never()).getPhoneNumberByContactName("x");
        assertNull(phoneNumber);
    }

    @Test
    public void givenLongPhoneNumber_whenRegister_thenFail() {
        given(phoneBookRepository.contains("x")).willReturn(false);
        willThrow(new RuntimeException())
                .given(phoneBookRepository).insert(any(String.class), eq(tooLongPhoneNumber));

        try {
            phoneBookService.register("x", tooLongPhoneNumber);
            fail("Should throw exception");
        } catch (RuntimeException ex) {
        }

        then(phoneBookRepository).should(never()).insert(contactName, tooLongPhoneNumber);
    }

    @Test
    public void givenEmptyPhoneNumber_whenRegister_thenFail() {
        phoneBookService.register(contactName, "");

        then(phoneBookRepository).should(never()).insert(contactName, "");
    }

    @Test
    public void givenExistentContactName_whenRegister_thenFail() {
        given(phoneBookRepository.contains(contactName))
                .willThrow(new RuntimeException("Name already exist"));

        try {
            phoneBookService.register(contactName, "012345");
            fail("Should throw exception");
        } catch (Exception ex) {
        }

        then(phoneBookRepository).should(never()).insert(contactName, "012345");
    }
}
