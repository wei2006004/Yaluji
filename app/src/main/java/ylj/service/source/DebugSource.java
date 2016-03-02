package ylj.service.source;

import java.util.Random;

public class DebugSource extends DataSource
{
	public final static int MAX_ADVALUE=50000;

	@Override
	public boolean start()
	{
		speed=1;
		state= STATE_HEADING;
		pulse=0;
		compassHeading=0;
		return true;
	}

	@Override
	public boolean stop()
	{
		return true;
	}

	@Override
	public boolean refresh()
	{
		pulse++;
		compassHeading+=100;
		compassPitch++;
		compassRoll++;
		
		Random random=new Random();
		temp=(int) (random.nextFloat()*MAX_ADVALUE);
		quake=(int) (random.nextFloat()*MAX_ADVALUE);
		return true;
	}

	@Override
	public void reset() {
		speed=1;
		state= STATE_HEADING;
		pulse=0;
		compassHeading=0;
	}

}
