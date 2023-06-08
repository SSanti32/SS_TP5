import pandas as pd
from matplotlib import pyplot as plt

sheets = ["200", "260", "320", "380"]
labels = ["d=1.2, N=200", "d=1.8, N=260", "d=2.4, N=320", "d=3.0, N=380"]
colors = ["tab:blue", "tab:orange", "tab:green", "tab:red"]
error_colors = ["lightblue", "peachpuff", "lightgreen", "lightcoral"]
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