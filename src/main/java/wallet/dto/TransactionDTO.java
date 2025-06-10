package wallet.dto;

import lombok.Data;

@Data
public class TransactionDTO {
    public String senderCPF;
    public String receiverCPF;
    public String value;
}
