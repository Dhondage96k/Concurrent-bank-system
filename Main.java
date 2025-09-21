import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Account {
    int accountId;
    String Name;
    int balance;

    public Account(int accountId, String name, int balance) {
        this.accountId = accountId;
        Name = name;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public synchronized void deposit(int amount) {
        balance += amount;
    }

    public synchronized boolean withdwadn(int amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

 

interface BankserviceInterface {
    void deposit(int accountId, int amount);

    void withdwadn(int accountId, int amount);

    void transfer(int accountId, int toaccount, int amount);

    void showBalane(int accountId);

}

class Bankservice implements BankserviceInterface {
Map<Integer , Account> accounts = new ConcurrentHashMap<>();

public void addAccount(Account account) {
    accounts.put(account.getAccountId(), account);
    System.out.println("New Account Addes Sucessully");
}


    @Override
    public void deposit(int accountId, int amount) {
Account ac = accounts.get(accountId);
if (ac!=null) {
    ac.deposit(amount);
    
}
    }

    @Override
    public void withdwadn(int accountId, int amount) {
        Account ac = accounts.get(accountId);
        if (ac != null) {
            ac.withdwadn(amount);    
            System.out.println("amout withdrawn");        
        }
          }

    @Override
    public void transfer(int accountId, int toaccount, int amount) {
        Account from = accounts.get(accountId);
        Account to = accounts.get(toaccount);
        synchronized(from){
            if (from !=null && to !=null) {
                 
                if (from.withdwadn(amount)) {
                    synchronized(to) {
                    to.deposit(amount);
                } 
                System.out.println("transferred");
            }
            }
        }
       
          }

    @Override
    public void showBalane(int accountId) {

        Account account = accounts.get(accountId);
        if (account!=null) {
            System.out.println(account.getBalance());           
        }
         }
}



public class Main {
    public static void main(String[] args) {

        Bankservice bankservice = new Bankservice();
        bankservice.addAccount(new Account(1, "Rohit", 99999999));
        bankservice.addAccount(new Account(2, "Raj", 1111110));
        bankservice.addAccount(new Account(3,"Samadhn ",1000000000));

        ExecutorService service = Executors.newFixedThreadPool(3);

        service.submit(() ->bankservice.deposit(1, 4000));
        service.submit(() ->bankservice.withdwadn(2, 50000));
        service.submit(() ->bankservice.transfer(1, 3, 37));

        service.shutdown();

        bankservice.showBalane(1);
        bankservice.showBalane(2);
        bankservice.showBalane(3);
    }
}