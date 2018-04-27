import tensorflow as tf

from tensorflow.examples.tutorials.mnist import input_data
ourmodel = input_data.read_data_sets("our_data/", one_hot=True, validation_size=0)

# Define hyper-parameters
learning_rate = 0.1
batch_size = 5
n_epochs = 10000

sess = tf.Session()
tf.logging.set_verbosity(tf.logging.ERROR)

W = tf.Variable(tf.zeros([784, 10]),name='W')
b = tf.Variable(tf.zeros([10]),name='b')

x = tf.placeholder(tf.float32, [None, 784],name='x')
y_ = tf.placeholder(tf.float32, [None, 10],name='y_')

y = tf.nn.softmax(tf.matmul(x, W) + b, name='y')

cross_entropy = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(logits=y, labels=y_))
train_step = tf.train.GradientDescentOptimizer(learning_rate).minimize(cross_entropy)

# save summaries for visualization
print("\nSaving summaries for visualization..")
tf.summary.histogram('weights', W)
tf.summary.histogram('max_weight', tf.reduce_max(W))
tf.summary.histogram('bias', b)
tf.summary.scalar('cross_entropy', cross_entropy)
tf.summary.histogram('cross_hist', cross_entropy)

# merge all summaries into one op
merged=tf.summary.merge_all()
trainwriter=tf.summary.FileWriter('projectdata/our_model'+'/logs/train',sess.graph)

init = tf.global_variables_initializer()
sess.run(init)

# Training the Model
print("\nTraining the model..")
for i in range(n_epochs):
    batch_xs, batch_ys = ourmodel.train.next_batch(batch_size)
    summary, _ = sess.run([merged, train_step], feed_dict={x: batch_xs, y_: batch_ys})
    trainwriter.add_summary(summary, i)

# Testing the model
print("\nTesting the model..")
# compare predicted label and actual label
correct_prediction = tf.equal(tf.argmax(y,1), tf.argmax(y_,1))

# accuracy op
accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))
accu=sess.run(accuracy, feed_dict={x: ourmodel.test.images, y_: ourmodel.test.labels})

# Print accuracy
print("Accuracy = " + str(accu))