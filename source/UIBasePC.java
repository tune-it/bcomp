
public class UIBasePC implements IUIBasePC
{
	private Frame ()
	{
		for (int i; i< size; i++)
		{
			UIBaseObject[i].Draw;
		}
	}
	
	private Init()
	{
		IUIBaseObject.Register=UIBasePCFabric.CreateRegister(...);
		UIBaseObject.push(alu);
		//... 
		//... 
		for (int i=0; i<size; i++)
		{
			IUIChannel[i]=UIBasePCFabric.CreateChannel(...);
		}
	}

}
