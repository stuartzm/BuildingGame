package us.rescyou.meme.state;

import java.util.HashMap;

import us.rescyou.meme.Config.StateID;

public class StateManager {

	/* Private Variables */

	private HashMap<StateID, State> states;
	private State currentState;

	/* Initialization */

	/**
	 * Creates a new StateManager.
	 */
	public StateManager() {
		states = new HashMap<StateID, State>();
	}

	/* State Management */
	
	public void addState(StateID stateID, State state) {
		getStates().put(stateID, state);
	}
	
	/* Setters */

	/**
	 * Sets the current State to be rendered / ticked.
	 * @param currentState
	 */
	private void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
	
	/**
	 * Transitions from the current State to the next State to be rendered / ticked.
	 * @param stateID
	 */
	public void enterState(StateID stateID) {
		setCurrentState(getStates().get(stateID));
	}
	
	/* Getters */
	
	/**
	 * @return the HashMap holding all of the Manager's States.
	 */
	public HashMap<StateID, State> getStates() {
		return states;
	}

	/**
	 * @return the State currently being rendered / ticked.
	 */
	public State getCurrentState() {
		return currentState;
	}

}
