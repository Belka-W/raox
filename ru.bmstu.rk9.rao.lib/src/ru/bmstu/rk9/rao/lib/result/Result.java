package ru.bmstu.rk9.rao.lib.result;

import ru.bmstu.rk9.rao.lib.json.JSONObject;
import ru.bmstu.rk9.rao.lib.naming.RaoNameable;
import ru.bmstu.rk9.rao.lib.simulator.CurrentSimulator;

public class Result<T> extends RaoNameable {

	public Result(AbstractDataSource<T> dataSource, ResultMode resultMode, Statistics<T> statistics) {
		this.dataSource = dataSource;
		this.resultMode = resultMode;
		this.statistics = statistics;
	}

	public Result(AbstractDataSource<T> dataSource, Statistics<T> statistics) {
		this(dataSource, ResultMode.AUTO, statistics);
	}

	public Result(AbstractDataSource<T> dataSource, ResultMode resultMode) {
		this(dataSource, resultMode, dataSource.getDefaultStatistics());
	}

	public Result(AbstractDataSource<T> dataSource) {
		this(dataSource, ResultMode.AUTO, dataSource.getDefaultStatistics());
	}

	public final JSONObject getData() {
		JSONObject datasetData = new JSONObject();
		datasetData.put("name", getName());
		statistics.updateData(datasetData);
		return datasetData;
	};

	public final void update() {
		if (!dataSource.condition())
			return;
		final T value = dataSource.evaluate();
		statistics.update(value, CurrentSimulator.getTime());
		CurrentSimulator.getDatabase().addResultEntry(this, value);
	};

	private final ResultMode resultMode;

	private final Statistics<T> statistics;

	private final AbstractDataSource<T> dataSource;

	public final ResultMode getResultMode() {
		return resultMode;
	}
}
