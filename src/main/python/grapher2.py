import pandas as pd
from matplotlib import pyplot as plt

sheets = ["200-1", "200-2", "200-3", "200-4", "200-5"]
labels = ["v=1m/s", "v=2m/s", "v=3m/s", "v=4m/s", "v=5m/s"]
colors = ["tab:blue", "tab:orange", "tab:green", "tab:red", "tab:pink"]
error_colors = ["lightblue", "peachpuff", "lightgreen", "lightcoral", "lightpink"]
index = 0

for sheet in sheets:
    df = pd.read_excel('resources/data.xlsx', sheet_name=sheet)
    times = list(df.loc[:, 'Time'])
    count = list(df.loc[:, 'Avg Count'])
    errors = list(df.loc[:, 'Stdev'])
    plt.errorbar(times, count, yerr=errors, label=labels[index], fmt='o', color=colors[index], ecolor=error_colors[index], elinewidth=3,capsize=0, linestyle='solid', linewidth=0.01, markersize=2)
    index += 1

plt.xlabel("Tiempo de salida (s)")
plt.ylabel("Cantidad de particulas salientes")
plt.legend()
plt.show()