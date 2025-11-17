# Issue 1.1 - Java fundamentals and OOO concepts

Launch the app. Explore the code.
Explain briefly these principles and find in the snake's code parts to illustrate them.

### Class, object, primitive types

A class is a blueprint for creating objects.
It defines the properties (attributes) and behaviors (methods) that the objects created from the class will have.
An object is an instance of a class.
A primitive type is a basic data type provided by the programming language, such as int, char, boolean, etc.

Example of class :
```java
public class Snake { // class name

    private final ArrayList<Cell> body; // property (attribute) of type ArrayList<Cell>
    private final AppleEatenListener onAppleEatenListener;
    private final Grid grid;
}
   ```
Example of object instantiation :
```java
GamePanel gamePanel = new GamePanel(); // new instance of GamePanel class
```
Example of primitive type :
```java
private int score; // primitive type int
```

### Encapsulation, properties, getter and setter, final.

Encapsulation is an object-oriented programming principle that restricts direct access to an object's data and methods.
Modifiers : `private`, `protected`,` public`).
Getters and setters are methods used to access and modify the properties of a class.
The final keyword is used to declare constants or to prevent method overriding and inheritance.
Example of encapsulation with private properties and getters/setters :
```java
public class Snake {
    private int score; // private property

    public int getScore() { // getter
        return score;
    }

    public void setScore(int score) { // setter
        this.score = score;
    }
}
```

### Instantiation of objects, Constructors.

Instantiation is the process of creating an instance (object) of a class using the `new` keyword.
A constructor is a special method that is called when an object is instantiated.

Example of instantiation and constructor :
```java
Snake snake = new Snake(grid, onAppleEatenListener); // instantiation using constructor
```

### Static fields, static methods. What are the particularity of "static" ?

Static fields and methods belong to the class itself rather than to any specific instance of the class.
This means that static fields and methods can be accessed without creating an instance of the class.
Example of static field and method :
```java
public static final int TILE_PIXEL_SIZE = 20; 
```

### Composition.

Composition represents a "has-a" relationship between classes, where one class contains references to other classes as its members.
Example of composition :
```java
public class Game {
    private final Grid grid; // Game "has-a" Grid
    }
```
  
### Inheritance, interface, polymorphism

Inheritance represents when a class is allowed to inherit the features(fields and methods) of another class.

Example of inheritance :
```java
public class OutOfPlayException extends Exception {}
// OutOfPlayException inherits from Exception class
```
Interface is a contract that defines a set of methods that a class must implement.

Example of interface :
```java
public interface AppleEatenListener {
    void onAppleEaten(Apple apple, Cell cell); // Any class that implements AppleEatenListener must provide an implementation for onAppleEaten method
}
```

Polymorphism allows objects of different classes to be treated as objects of a common superclass.
Example of polymorphism :
```java
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            UIUtils.draw(g, game);
        } else {
            gameOver(g);
        }
    }
```
### Static VS dynamic types.

For static typing, variable types are known at compile time.
For dynamic typing, variable types are known at runtime.
Example of static typing :
```java
int x;
```

### Separation of concerns (design principle)

Separation of concerns is a principle of software design that the source code be separated into layers and components that each have distinct functionality with as little overlap as possible

### Collections

Collections are data structures that can hold multiple values or objects.
Example of collection :
```java
public class Basket {
    private Grid grid;
    private List<Apple> apples; // List is a collection
}
```

### Exceptions

Exceptions are events that are thrown during the execution of a program that stop the normal flow of instructions.
Example of exception handling :
```java
try
        {
    game.iterate(direction);
        } 
catch (OutOfPlayException | SelfCollisionException exception)
        {
                timer.stop();
                running = false;
        }
```

### Functional interfaces / Lambda

A functional interface is an interface that has only one abstract method.
A lambda expression is a concise way to represent an anonymous function that can be passed around as a value.
Example of functional interface and lambda expression :
```java
public interface AppleEatenListener { 
    void onAppleEaten(Apple apple, Cell cell); // single abstract method
}
snake = new Snake((apple, cell) -> basket.removeAppleInCell(apple,cell), grid); // lambda expression 
```

### Lombok https://projectlombok.org/

Lombok is a Java library that helps to reduce boilerplate code by generating common methods like getters, setters, constructors, etc., at compile time using annotations.
Example of Lombok usage :
```java
@Getter
public class Apple {
    public Apple() {
    }
}
```