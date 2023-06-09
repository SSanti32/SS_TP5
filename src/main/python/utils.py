import numpy as np

def calculate_flow_rate(filename):
    flow_rate = []
    for i in range(1, 4):
        with open("../resources/" + filename + str(i) + ".txt") as file:
            flow_rate_aux = []
            for line in file:
                line_aux = line.split()
                flow_rate_aux.append(float(line_aux[0]))
            flow_rate.append(flow_rate_aux)
        file.close()

    return np.array(flow_rate), np.std(flow_rate, axis=0)

def calculate_caudal(flow, caudal, interval):
    for num in np.mean(flow, axis=0):
        for possible_values in range(0,69):
                if (num > possible_values and num < interval + possible_values):
                    if caudal[possible_values] == 0 or caudal[possible_values] is None:
                            caudal[possible_values] = 0
                    caudal[possible_values] += 1/interval