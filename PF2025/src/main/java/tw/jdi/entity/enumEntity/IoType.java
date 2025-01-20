package tw.jdi.entity.enumEntity;

public enum IoType{
	
	DI(ViewType.VIEW), 
	DO(ViewType.CONTROL), 
	AI(ViewType.VIEW), 
	AO(ViewType.CONTROL);
	
	private ViewType viewType;
	
	IoType(ViewType viewType) {
		this.viewType = viewType;
	}
	
	public ViewType getViewType() {
		return viewType;
	}
	
	
	public enum ViewType {
		VIEW, CONTROL
	}
}
