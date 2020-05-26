### Final Project
### STAT 3050
### Meghan Buscher
### Last updated 05/16/2020

## Load in libraries
library(tidyverse)
library(ggplot2)
library(ggfortify)
library(car)
library(leaps)
library(psych)

## Read in data
bear <- read.csv("bear_Edited.csv")

## Plot Weight vs. Age
ggplot(
  data = bear,
  aes(
    x = Age,
    y = Weight,
    col = Sex
  )
) + geom_point(
  
) + geom_smooth(
  method = "lm",
  col = 'black',
  alpha = 0.25
) + geom_smooth(
  method = "lm",
  aes(
    col = Sex
  ),
  se = FALSE,
  linetype = 'dashed'
) + labs(
  x = "Age (in months)",
  y = "Weight (kg)",
  title = "Weight of Brown Bears vs. Age"
) + theme_classic() 

## Make boxplot of sex vs weight
ggplot(
  data = bear,
  aes(
    x = Sex,
    y = Weight
  )
) + stat_boxplot(
    geom = "errorbar"
) + geom_boxplot(
  fill = 'cadetblue4'
) + labs(
  y = "Weight (kg)",
  title = "Weight of Brown Bears vs. Sex"
) + theme_classic()

## Plot Weight vs. Head Width
ggplot(
  data = bear,
  aes(
    x = Head.Width,
    y = Weight,
    col = Sex
  )
) + geom_point(
  
) + geom_smooth(
  method = "lm",
  col = 'black',
  alpha = 0.25
) + geom_smooth(
  method = "lm",
  aes(
    col = Sex
  ),
  se = FALSE,
  linetype = 'dashed'
) + labs(
  x = "Head Width (cm)",
  y = "Weight (kg)",
  title = "Weight of Brown Bears vs. Head Width"
) + theme_classic()

## Plot Weight vs. Head Length
ggplot(
  data = bear,
  aes(
    x = Head.Length,
    y = Weight,
    col = Sex
  )
) + geom_point(
  
) + geom_smooth(
  method = "lm",
  col = 'black',
  alpha = 0.25
) + geom_smooth(
  method = "lm",
  aes(
    col = Sex
  ),
  se = FALSE,
  linetype = 'dashed'
) + labs(
  x = "Head Length (cm)",
  y = "Weight (kg)",
  title = "Weight of Brown Bears vs. Head Length"
) + theme_classic()

## Plot Weight vs. Neck Girth
ggplot(
  data = bear,
  aes(
    x = Neck.Girth,
    y = Weight,
    col = Sex
  )
) + geom_point(
  
) + geom_smooth(
  method = "lm",
  col = 'black',
  alpha = 0.25
) + geom_smooth(
  method = "lm",
  aes(
    col = Sex
  ),
  se = FALSE,
  linetype = 'dashed'
) + labs(
  x = "Neck Girth (cm)",
  y = "Weight (kg)",
  title = "Weight of Brown Bears vs. Neck Girth"
) + theme_classic()

## Plot Weight vs. Body Length
ggplot(
  data = bear,
  aes(
    x = Body.Length,
    y = Weight,
    col = Sex
  )
) + geom_point(
  
) + geom_smooth(
  method = "lm",
  col = 'black',
  alpha = 0.25
) + geom_smooth(
  method = "lm",
  aes(
    col = Sex
  ),
  se = FALSE,
  linetype = 'dashed'
) + labs(
  x = "Body Length (m)",
  y = "Weight (kg)",
  title = "Weight of Brown Bears vs. Body Length"
) + theme_classic()

## Plot Weight vs. Chest Girth
ggplot(
  data = bear,
  aes(
    x = Chest.Girth,
    y = Weight,
    col = Sex
  )
) + geom_point(
  aes(
    col = Sex
  )
) + geom_smooth(
  method = "lm",
  col = 'black',
  alpha = 0.25
) + geom_smooth(
  method = "lm",
  aes(
    col = Sex
  ),
  se = FALSE,
  linetype = 'dashed'
) + labs(
  x = "Chest Girth (cm)",
  y = "Weight (kg)",
  title = "Weight of Brown Bears vs. Chest Girth"
) + theme_classic()

## Get summary of data
summary(bear)

## Find best subsets
best_subs1 <- regsubsets(
  Weight ~ Age + Head.Width + Head.Length + Neck.Girth + Body.Length + Chest.Girth + Sex,
  data = bear
)
subsets(
  object = best_subs1,
  statistic = "adjr2"
)

## Add log variables
bear$Age_log <- log(bear$Age)
bear$Head.Width_log <- log(bear$Head.Width)
bear$Head.Length_log <- log(bear$Head.Length)
bear$Neck.Girth_log <- log(bear$Neck.Girth)
bear$Body.Length_log <- log(bear$Body.Length)
bear$Weight_log <- log(bear$Weight)
bear$Chest.Girth <- log(bear$Chest.Girth)

## Get rid of unnecessary data 
## (i.e. data that is not relevant to our plot)
bear_subset <- bear[,5:18]

## Find best subsets (expanded)
best_subs2 <- regsubsets(
  Weight ~ . - Weight_log,
  data = bear_subset
)
subsets(
  object = best_subs2,
  statistic = "adjr2"
)

## Find best subsets (expanded) (log)
best_subs3 <- regsubsets(
  Weight_log ~ . - Weight,
  data = bear_subset
)
subsets(
  object = best_subs3,
  statistic = "adjr2"
)

# create models
## Model 1
model1 <- lm(
  Weight ~ Age + Head.Width + Head.Length + Neck.Girth + Body.Length + Chest.Girth + Sex,
  data = bear
)
summary.lm(model1)
autoplot(
  model1,
  smooth.colour = NA
) + theme_classic()
## Model 2
model2 <- lm(
  log(Weight) ~ Age + Head.Width + Head.Length + Neck.Girth + Body.Length + Chest.Girth + Sex,
  data = bear
)
summary.lm(model2)
autoplot(
  model2,
  smooth.colour = NA
) + theme_classic()
## Model 3
model3 <- lm(
  log(Weight) ~ Age + log(Head.Width) + log(Head.Length) + Neck.Girth + Body.Length + Chest.Girth + Sex,
  data = bear
)
summary.lm(model3)
autoplot(
  model3,
  smooth.colour = NA
) + theme_classic()
## Model 4
model4 <- lm(
  log(Weight) ~ Age + log(Head.Width) + log(Head.Length) + log(Neck.Girth) + Body.Length + Chest.Girth + Sex,
  data = bear
)
summary.lm(model4)
autoplot(
  model4,
  smooth.colour = NA
) + theme_classic()
## Model 5
model5 <- lm(
  log(Weight) ~ Age + log(Head.Length) + Neck.Girth + Body.Length + Chest.Girth + Sex,
  data = bear
)
summary.lm(model5)
autoplot(
  model5,
  smooth.colour = NA
) + theme_classic()
## Model 6
model6 <- lm(
  log(Weight) ~ Age + log(Head.Length) + Neck.Girth + Body.Length + Chest.Girth,
  data = bear
)
summary.lm(model6)
autoplot(
  model6,
  smooth.colour = NA
) + theme_classic()
## Model 7
model7 <- lm(
  log(Weight) ~ Age + Body.Length + Sex,
  data = bear
)
summary.lm(model7)
autoplot(
  model7,
  smooth.colour = NA
) + theme_classic()
## Model 8
model8 <- lm(
  log(Weight) ~ Age + I(Age^2) + Body.Length + Sex,
  data = bear
)
summary.lm(model8)
autoplot(
  model8,
  smooth.colour = NA
) + theme_classic()
## Model 9
model9 <- lm(
  log(Weight) ~ Age + I(Age^2) + Body.Length + Sex + Chest.Girth,
  data = bear
)
summary.lm(model9)
autoplot(
  model9,
  smooth.colour = NA
) + theme_classic()
## Model 10
model10 <- lm(
  log(Weight) ~ Age + I(Age^2) + Body.Length + I(Body.Length^2) + Sex + Chest.Girth,
  data = bear
)
summary.lm(model10)
autoplot(
  model10,
  smooth.colour = NA
) + theme_classic()
## Histogram of Model 10
ggplot(
  data = NULL,
  aes(
    x = resid(model10)
  )
) + 
  geom_histogram(
    bins = 12,
    fill = "cadetblue4",
    col = "black"
  ) + 
  labs(
    x = "Residuals",
    title = "Histogram of Residuals"
  ) + 
  theme_classic(
    
  )
## Get Confidence Interval for Model 10
confint(model10)
