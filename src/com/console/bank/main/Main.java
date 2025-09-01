package com.console.bank.main;
import com.console.bank.entity.User;
import com.console.bank.service.UserService;

import java.util.*;
public class Main {
    private static Scanner sc=new Scanner(System.in);
    static  Main main = new Main();
    static UserService userService = new UserService();
    public static void main(String[] args) {

        while (true) {
            System.out.println("Enter your username");
            String username = sc.next();

            System.out.println("Enter your password");
            String password = sc.next();

            User user = userService.login(username, password);
            if (user != null && user.getRole().equals("admin")) {
                main.initAdmin();
            } else if (user != null && user.getRole().equals("user")) {
                main.initCustomer(user);
            } else {
                System.out.println("Login failed");
            }

        }
    }

    private void initAdmin() {

        boolean flag = true;
        String userId= "";
        while (flag) {
            System.out.println("1. Exit/Logout");
            System.out.println("2. Create an new account");
            System.out.println("3. See all transactions");
            System.out.println("4. Check Account Balance");
            System.out.println("5. Approve cheque book request");

            int selectedOption = sc.nextInt();

            switch (selectedOption) {
                case 1:
                    flag=false;
                    System.out.println("you have successfully logged out...");
                    break;
                case 2:
                    main.addNewCustomer();
                    break;
                case 3:
                    System.out.println("Enter user id");
                    userId=sc.next();
                    printTransactions(userId);
                    break;
                case 4:
                    System.out.println("Enter user id");
                    userId=sc.next();
                    Double accountBalance=checkBankBalance(userId);
                    System.out.println("The user Balance is: "+accountBalance);
                    break;
                case 5:
                    List<String> userIds=getUserIdForCheckBookRequest();
                    System.out.println("Please choose user id from below:");
                    System.out.println(userIds);

                    userId=sc.next();

                    approveChequeBookRequest(userId);
                    System.out.println("Cheque book request is approved..");
                    break;
                default:
                    System.out.println("Wrong choice");
            }
        }
    }
    private void approveChequeBookRequest(String userId){
        userService.approveChequeBookRequest(userId);
    }
    private List<String> getUserIdForCheckBookRequest(){
        return userService.getUserIdForCheckBookRequest();
    }
    private void addNewCustomer(){
        System.out.println("Enter your username:");
        String username=sc.next();
        System.out.println("Enter password");
        String password=sc.next();
        System.out.println("Enter Contact Number");
        String contact=sc.next();

        boolean result=userService.addNewCustomer(username,password,contact);
        if(result){
            System.out.println("Customer account is created...");
        }
        else{
            System.out.println("Customer account creation is failed...");
        }
    }
    private void initCustomer(User user){
        boolean flag = true;
        while (flag) {
            System.out.println("1. Exit/Logout");
            System.out.println("2. Check bank balance");
            System.out.println("3. Fund Transfer");
            System.out.println("4. See all transactions");
            System.out.println("5. Raise chequebook request");

            int selectedOption = sc.nextInt();

            switch (selectedOption) {
                case 1:
                    flag=false;
                    System.out.println("you have successfully logged out...");
                    break;
                case 2:
                    Double balance=main.checkBankBalance(user.getUsername());
                    if(balance!=null){
                        System.out.println("Your bank balance is "+balance);
                    }
                    else{
                        System.out.println("Check your username");
                    }
                    break;
                case 3:
                    main.fundTransfer(user);
                    break;
                case 4:
                    main.printTransactions(user.getUsername());
                    break;
                case 5:
                    String userId=user.getUsername();
                    Map<String,Boolean> map=getAllChequebookRequest();
                    if(map.containsKey(userId) && map.get(userId)){
                        System.out.println("You have already raised a request and it is already approved");
                    }
                    else if(map.containsKey(userId) && !map.get(userId)){
                        System.out.println("you have already raised a request and it is pending for approval");
                    }
                    else{
                        raiseChequeBookRequest(userId);
                        System.out.println("Request raised successfully..");
                    }
                    break;
                default:
                    System.out.println("Wrong choice");
            }
        }
    }
    private Map<String, Boolean> getAllChequebookRequest(){
        return userService.getAllChequebookRequest();
    }
    private void raiseChequeBookRequest(String userId){
        userService.raiseChequeBookRequest(userId);
    }
    private void printTransactions(String userId){
        userService.printTransactions(userId);
    }

    private void fundTransfer(User userDetails){
        System.out.println("Enter payee account user id");
        String payeeAccountId=sc.next();
        User user=getUser(payeeAccountId);
        if(user!=null){
            System.out.println("Enter amount to transfer");
            Double amount=sc.nextDouble();
            Double userAccountBalance=checkBankBalance(userDetails.getUsername());
            if(userAccountBalance>=amount){
                boolean result=userService.transferAmount(userDetails.getUsername(),payeeAccountId,amount);
                if(result){
                    System.out.println("Amount transferred successfully...");
                }
                else{
                    System.out.println("Transfer failed");
                }

            }
            else{
                System.out.println("Your balance is insufficient: "+userAccountBalance);
            }
        }
        else{
            System.out.println("Enter valid username");
        }

    }
    private User getUser(String userId){
        return userService.getUser(userId);
    }

    private Double checkBankBalance(String userId){
         return userService.checkBankBalance(userId);
    }
}
