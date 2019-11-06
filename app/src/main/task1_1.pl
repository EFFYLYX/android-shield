import pandas as pd
import matplotlib.pyplot as plt

csv_data = pd.read_csv('irismissing.csv')

[row_size, column_size] = csv_data.shape

row_nan = []
colum_nan = []

# traverse the data frame
for i in range(row_size):

    for j in csv_data.keys():
        # if the value is NA (missing values), put the row number into a list
        if pd.isna(csv_data[j][i]):
            row_nan.append(i)
            # if the the column index does not appear in the list before, add it
            if j not in colum_nan:
                colum_nan.append(j)
