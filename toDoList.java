package com.prac3;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.time.*;


public class toDoList {
    public static void main(String[] args){
        // Creating a new file
        File myFile = new File("info.txt");
        try{
            boolean res = myFile.createNewFile();
        }
        catch (IOException e){
            System.out.println("Unable to create the file!");
            return;
        }

        // Main program loop
        Todo myList = new Todo();
        boolean continueProgram = true;

        while (continueProgram){
            myList.printTodo(myFile);
            continueProgram = myList.getUserInput(myFile);
        }

    }
}


class Todo{
    private static int lineCount = 0;


    public void printTodo(File myFile) {
        try {
            if (myFile.exists()) {
                Scanner sc = new Scanner(myFile);
                int i = 0;
                System.out.println("Current TODO List:");
                while (sc.hasNextLine()) {
                    String data = sc.nextLine();
                    System.out.println(String.valueOf(++i) + ": " + data);
                }
                lineCount = i;
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("Error processing!" + e.getMessage());
        }
    }


    public boolean getUserInput(File myFile){
        Scanner sc = new Scanner(System.in);
        int choice;
        while (true){
            System.out.println("Select from the following:\n1 - Enter a task\n2 - Delete a task\n3 - Exit program");
            try{
                choice = sc.nextInt();
                sc.nextLine(); // to consume the newline char
                if (choice != 1 && choice != 2 && choice != 3){
                    System.out.println("Invalid input!");
                } else if (choice == 3) {
                    System.out.println("Exiting...");
                    return false;
                }
                else if (choice == 2){
                    //delete a task
                    if (lineCount == 0){
                        System.out.println("No task to delete.");
                        return true;
                    }
                    int delLine;
                    while (true){
                        try{
                            System.out.println("Enter the task number to delete: ");
                            delLine = sc.nextInt();
                            if (delLine < 1 || delLine > lineCount){
                                System.out.println("Task number must be between 1 and " + lineCount + ".");
                            }
                            else{
                                //delete the task number
                                if (deleteLine(myFile, delLine)){
                                    System.out.println("Task " + delLine + " successfully deleted!");
                                }
                                else{
                                    System.out.println("Unable to remove the task at the moment.");
                                }
                                break;
                            }
                        }
                        catch (Exception e){
                            System.out.println("Invalid input - " + e.getMessage());
                        }
                    }
                    break;
                }
                else {
                    System.out.print("--> ");
                    String task = sc.nextLine();
                    //enter the string into the file along with date and some other data
                    LocalDateTime myDate = LocalDateTime.now();
                    DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss");
                    String dateStr = myDate.format(myFormatter);
                    task += " (" + dateStr + ")\n";
                    try{
                        FileWriter fout = new FileWriter(myFile.getName(), true);
                        fout.write(task);
                        fout.close();
                    }
                    catch (IOException e){
                        System.out.println("File write error - " + e.getMessage());
                    }
                    break;
                }
            }
            catch (Exception e){
                sc.nextLine(); // to consume the newline char
                System.out.println("Invalid input - " + e.getMessage());
            }
        }
        return true;
    }


    private boolean deleteLine(File myFile, int lineNum){
        try {
            if (myFile.exists()) {
                Scanner sc = new Scanner(myFile);
                String concatStr = "", newStr = "";
                while (sc.hasNextLine()) {
                    String data = sc.nextLine();
                    concatStr += (data + '\n');
                }
                String regex = "[\n]";
                String[] arr = concatStr.split(regex);

                FileWriter fout = new FileWriter(myFile.getName()); // this will overwrite the data to delete the task
                for (int i = 0; i < arr.length; i++){
                    if (i != lineNum-1){
                        newStr += (arr[i] + "\n");
                    }
                }
                System.out.println(newStr);
                fout.write(newStr);
                fout.close();
            }
        } catch (IOException e) {
            System.out.println("Error processing!" + e.getMessage());
            return false;
        }
        return true;
    }
}