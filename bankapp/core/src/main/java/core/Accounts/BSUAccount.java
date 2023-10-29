package core.Accounts;

import java.io.IOException;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import core.Profile;

@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "@id")
public class BSUAccount extends AbstractAccount implements Serializable {
  public static final String file = "bankapp/core/src/main/java/json/TransactionsOverview.json";

  public BSUAccount(@JsonProperty("name") String name, @JsonProperty("profile") Profile profile) {
    super(name, profile);
  }

  @Override
  public void transferTo(AbstractAccount account, int amount, String file) throws IOException {
    throw new IllegalArgumentException("You can't take money out of a BSU account");
  }

}
