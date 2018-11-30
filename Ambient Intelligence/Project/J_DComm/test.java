//package DComm;

import DComm.DCommAPI;


public class test {

  public static void main(String[] args) {

    int status;
    byte[] value_p = {(byte)0x05, (byte)0x00, (byte)0x00, (byte)0x00};


    DCommAPI api = DCommAPI.init("c1.txt");

    api.sendGet(2, 0, 0, 1, 3);

//    api.sendNotify(2, 1, 2, value_p);
    api.sendNotify(1, 2, value_p);

    api.processMsg();

    api.sendSet(2, 0, 0, 1, 1, value_p);

    api.processMsg();

    api.processMsg();
    api.processMsg();
  }

}
