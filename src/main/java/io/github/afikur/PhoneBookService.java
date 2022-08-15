package io.github.afikur;

public class PhoneBookService {
    private final PhoneBookRepository phonebookRepository;

    public PhoneBookService(PhoneBookRepository phonebookRepository) {
        this.phonebookRepository = phonebookRepository;
    }

    public void register(String name, String phone) {
        if (!name.isEmpty() && !phone.isEmpty() && !phonebookRepository.contains(name)) {
            phonebookRepository.insert(name, phone);
        }
    }

    public String search(String name) {
        if (!name.isEmpty() && phonebookRepository.contains(name)) {
            return phonebookRepository.getPhoneNumberByContactName(name);
        }
        return null;
    }
}
