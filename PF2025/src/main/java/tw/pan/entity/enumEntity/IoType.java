package tw.pan.entity.enumEntity;

public enum IoType {
	
	DI(SignalType.DIGITAL, ViewType.VIEW), 
	DO(SignalType.DIGITAL, ViewType.CONTROL), 
	AI(SignalType.ANALOG, ViewType.VIEW), 
	AO(SignalType.ANALOG, ViewType.CONTROL);
	
	private SignalType signalType;
	private ViewType viewType;
	
	IoType(SignalType signalType, ViewType viewType) {
		this.signalType = signalType;
		this.viewType = viewType;
	}
	
	public SignalType getSignalType() {
		return signalType;
	}
	
	public ViewType getViewType() {
		return viewType;
	}
	
	
	public enum ViewType {
		VIEW, CONTROL
	}
	
	public enum SignalType{
		ANALOG, DIGITAL
	}
}
