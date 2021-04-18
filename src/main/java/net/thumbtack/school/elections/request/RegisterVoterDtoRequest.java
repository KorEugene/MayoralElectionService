package net.thumbtack.school.elections.request;

import net.thumbtack.school.elections.model.Account;
import net.thumbtack.school.elections.model.Address;
import net.thumbtack.school.elections.model.FullName;

public class RegisterVoterDtoRequest {

    private FullName fullName;
    private Address address;
    private Account account;

    public RegisterVoterDtoRequest(FullName fullName, Address address, Account account) {
        this.fullName = fullName;
        this.address = address;
        this.account = account;
    }

    public FullName getFullName() {
        return fullName;
    }

    public void setFullName(FullName fullName) {
        this.fullName = fullName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "RegisterVoterDtoRequest{" +
                ", fullName=" + fullName +
                ", address=" + address +
                ", account=" + account +
                '}';
    }
}
