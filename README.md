# akkaBehavior

## Introduction

A behavior is an actor that represent a task or a simple action.  
There are several types of behaviors:  
* OneShotBehavior (behavior that is used only once)
* TimerBehavior (behavior that represent an task that is executed after a certain ammount of time)
* TickerBehavior (behavior that is executed repeatedly)
* ParallelBehavior (behavior that execute several behaviors asynchronously)

Every behavior is monitored by a **supervisor** Actor.  
Before Ending the behavior passes throught several state that are *Idle*, *Running* (or *ComplexRunning*), *FinishedRun* and *Ended*.  
If you want to stop you shall send it a Stop Message therefore it will go directly to the state Killed.  
When the behavior is Ended it sends a message Finished to its supervisor.  
If the behavior is Killed it sends a message Dead to its supervisor.  

## simple Usage
You shall use those behavior by two ways:  
* Using the class constructor or by derivated classes
* Throught the companion object (*apply* method)

## Complex Usage
It is possible to create user-defined behaviors.  
The user must define a class that extends the ComplexBehavior
To do that you must go to ComplexRunning state.  
Example:  

```scala
class MyComplexBehavior extends ComplexBehavior(toRun:() =>Unit){
 
  
  override final protected def init = {}
  
  def complexRun = {
     // TODO: user definet
     toRun()
     println("---------")
     toRun()
   }
  
  when(ComplexRunning)
  {
    //this handler must exist
    case Event(ComplexRun, _) => complexRun
                                  stay

     //Once the complex run is finished you must go to state Ended	
    case Event(FinishedRun, _) => self ! Poke
                                  goto(Ended) 

  }
}


```
