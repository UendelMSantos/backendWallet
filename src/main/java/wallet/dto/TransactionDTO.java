package wallet.dto;

import lombok.Data;

@Data
public class TransactionDTO {
    public String senderCPF;
    public String receiverCPF;
    public long value;

    public TransactionDTO() {}

    public TransactionDTO(String senderId, String receiverId, long value) {
        this.senderCPF = senderId;
        this.receiverCPF = receiverId;
        this.value = value;
    }
}
