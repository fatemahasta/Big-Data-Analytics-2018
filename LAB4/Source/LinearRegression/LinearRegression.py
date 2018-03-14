from __future__ import print_function
import tensorflow as tf
import numpy as np
import matplotlib.pyplot as plt
rng = np.random
import pandas as pd
#boston = load_boston()

data = np.genfromtxt('features1.txt',delimiter=',')
features = np.array(data)
bos = pd.DataFrame(data)
#print(features)
#print(bos)
trX = np.array(data[:,][:,0])
trY = np.array(data[:,][:,1:5])
#print(trX)
#print(trY)
# create symbolic variables
X = tf.placeholder("float")
Y = tf.placeholder("float")

# create a shared variable for the weight matrix
w = tf.Variable(rng.randn(), name="weights")
b = tf.Variable(rng.randn(), name="bias")

# prediction function
y_model = tf.add(tf.multiply(X, w), b)

# Mean squared error
cost = tf.reduce_sum(tf.pow(y_model-Y, 2))/(2*trX.shape[0])

# construct an optimizer to minimize cost and fit line to my data
train_op = tf.train.GradientDescentOptimizer(0.5).minimize(cost)

# Launch the graph in a session
sess = tf.Session()

# Initializing the variables
init = tf.global_variables_initializer()

# you need to initialize variables
sess.run(init)
for i in range(20):
    for (x, y) in zip(trX, trY):
        sess.run(train_op, feed_dict={X: x, Y: y})

print("Optimization Finished!")
training_cost = sess.run(cost, feed_dict={X: trX, Y: trY})

print("Training cost=", training_cost, "W=", sess.run(w), "b=", sess.run(b), '\n')

# Testing or Inference
data1 = np.genfromtxt('testfeatures1.txt',delimiter=',')

testfeatures = np.array(data1)
bos1 = pd.DataFrame(data1)
#print(features)
#print(bos)
test_X = np.array(data1[:,][:,0])
test_Y = np.array(data1[:,][:,1:5])

#test_X = np.asarray([rng.randn(),rng.randn()])
#test_Y = sess.run(w)*test_X + sess.run(b)
print("Testing... (Mean square loss Comparison)")

testing_cost = sess.run(
    tf.reduce_sum(tf.pow(y_model - Y, 2)) / (2 * test_X.shape[0]),
    feed_dict={X: test_X, Y: test_Y}) # same function as cost above
print("Testing cost=", testing_cost)
print("Absolute mean square loss difference:", abs(
    training_cost - testing_cost))

plt.plot(trX, trY,'ro', label='Original data')
plt.plot(trX, sess.run(w) * trX + sess.run(b), label='Fitted Line')
#plt.legend(loc='upper left')
plt.ylabel('Measured')
plt.xlabel('Predicted')
plt.show()