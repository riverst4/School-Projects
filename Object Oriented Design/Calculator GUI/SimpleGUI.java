import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class SimpleGUI implements ActionListener {
   private static JTextField inputBox;
   private JFrame frame;

   SimpleGUI(){}
   public static void main(String[] args) {
	   SimpleGUI gui = new SimpleGUI();
      gui.run();
   }

   private void run() {          
      JFrame frame = new JFrame("Calculator");
      frame.setSize(500, 500); 
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLayout(new GridLayout(0, 1));

      createUI(frame);          
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }

   private static void createUI(JFrame frame) {
      JPanel panel = new JPanel(new GridLayout(4,4,4,4));
      SimpleGUI calculator = new SimpleGUI();
      GridBagLayout layout = new GridBagLayout();          
      GridBagConstraints gbc = new GridBagConstraints();
      panel.setLayout(layout);
       
      inputBox = new JTextField(10);        
      inputBox.setEditable(false);

      JButton button0 = new JButton("0");
      JButton button1 = new JButton("1");
      JButton button2 = new JButton("2");
      JButton button3 = new JButton("3");
      JButton button4 = new JButton("4");
      JButton button5 = new JButton("5");
      JButton button6 = new JButton("6");
      JButton button7 = new JButton("7");
      JButton button8 = new JButton("8");
      JButton button9 = new JButton("9");

      JButton plusButton = new JButton("+");
      JButton minusButton = new JButton("-");
      JButton clearButton = new JButton("C");
      JButton buttonEqualed = new JButton("=");
      
      frame.getContentPane().add(BorderLayout.CENTER, panel);

      frame.setVisible(true);


      button1.addActionListener(calculator);
      button2.addActionListener(calculator);
      button3.addActionListener(calculator);
      button4.addActionListener(calculator);
      button5.addActionListener(calculator);
      button6.addActionListener(calculator);
      button7.addActionListener(calculator);
      button8.addActionListener(calculator);
      button9.addActionListener(calculator);
      button0.addActionListener(calculator);
      plusButton.addActionListener(calculator);
      minusButton.addActionListener(calculator);
      clearButton.addActionListener(calculator);
      buttonEqualed.addActionListener(calculator);

      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.gridx = 0; gbc.gridy = 0;panel.add(button1, gbc);        
      gbc.gridx = 1; gbc.gridy = 0; panel.add(button2, gbc);
      gbc.gridx = 2; gbc.gridy = 0; panel.add(button3, gbc);
      gbc.gridx = 3; gbc.gridy = 0; panel.add(plusButton, gbc);
      gbc.gridx = 0; gbc.gridy = 1; panel.add(button4, gbc);
      gbc.gridx = 1; gbc.gridy = 1; panel.add(button5, gbc); 
      gbc.gridx = 2; gbc.gridy = 1; panel.add(button6, gbc);
      gbc.gridx = 3; gbc.gridy = 1; panel.add(minusButton, gbc);
      gbc.gridx = 0; gbc.gridy = 2; panel.add(button7, gbc);
      gbc.gridx = 1; gbc.gridy = 2; panel.add(button8, gbc);
      gbc.gridx = 2; gbc.gridy = 2; panel.add(button9, gbc);
      gbc.gridx = 1; gbc.gridy = 3; panel.add(button0, gbc);
      gbc.gridx = 2; gbc.gridy = 3; panel.add(clearButton, gbc);
      gbc.gridwidth = 3;

      gbc.gridx = 0; gbc.gridy = 4; panel.add(inputBox, gbc);        
      gbc.gridx = 3; gbc.gridy = 4; panel.add(buttonEqualed, gbc);
      frame.getContentPane().add(panel, BorderLayout.CENTER);        
   }

   public void actionPerformed(ActionEvent e) {
      String command = e.getActionCommand();
      if (command.charAt(0) == 'C') {                      
         inputBox.setText("");
      }else if (command.charAt(0) == '=') {   
    	  
         inputBox.setText(solveExpression(inputBox.getText()));
    	  
      }else {                                
         inputBox.setText(inputBox.getText() + command);
      }
   }
 
   public static String solveExpression(String expression) {
      
      
      String op1 = "";String op2 = "";String operator = "";
      double result = 0;
      boolean oper = false;
      boolean negative = false;
      
      
      for(int i = 0; i < expression.length();i++) {
    	  
    	  
    	  //we need to do some checks and/or set the operator
    	  if(expression.charAt(i) == '-') {
    		  if(i == 0) {
    			  negative = true;
    		  } else {
    			  if(op1 != "" && op2 != "" ) {
    	    		  double temp = 0;
    	    		  if(operator.equals("-")) {
    	    			  temp = (Double.parseDouble(op1) - Double.parseDouble(op2));
    	    		  } else if(operator.equals("+")) {
    	    			  temp = (Double.parseDouble(op1) + Double.parseDouble(op2));
    	    		  }
    	    		  
    	    		  op1 = temp + "";
    	    		  op2 = "";
    	    	  } else {
    	    		  operator = "-";
    	    		  oper = true;
    	    	  }
    		  }
    		  //we need to do checks and/or set the operator
    	  } else if(expression.charAt(i) =='+') {
    		  //if operator 1 has an item and operator 2 has an item
    		  if(op1 != "" && op2 != "" ) {
        		  double temp = 0;
        		  //checks sign
        		  if(operator.equals("-")) {
        			  temp = (Double.parseDouble(op1) - Double.parseDouble(op2));
        		  } else if(operator.equals("+")) {
        			  temp = (Double.parseDouble(op1) + Double.parseDouble(op2));
        		  }
        		  //trim the expression via math
        		  op1 = temp + "";
        		  op2 = "";
        	  } else {
        		  //first operator
        		  operator = "+";
        		  oper = true;
        	  }
    		  
    		  
    	  }
    	  //if the expression is a number to be calculated
    	  if(expression.charAt(i) >= '0' && expression.charAt(i) <= '9' || (expression.charAt(i) == '.')) {
    		  if(oper) {
    			  op2 += expression.charAt(i);
    		  } else {
    			  op1 += expression.charAt(i);
    		  }
    	  }
    	  
    	  
      }
      
      
      //add the two numbers
      if (operator.equals("+")) {
         //if the first number is negative account for it
         if(negative == true) {
        	 result = (-Double.parseDouble(op1) + Double.parseDouble(op2));
         } else {
        	 result = Double.parseDouble(op1) + Double.parseDouble(op2);
         }
    	  //subtract the two numbers
      } else if (operator.equalsIgnoreCase("-")) {
    	  //if the first number is negative
    	  if(negative == true) {
    		  result = (-Double.parseDouble(op1)- Double.parseDouble(op2));
    	  }
         result = (Double.parseDouble(op1) -(Double.parseDouble(op2)));       
    	  
      
      }
      
      return result + "";
   }
}
