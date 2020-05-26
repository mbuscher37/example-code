### Midterm Project
### STAT 3050
### Meghan Buscher
### Last updated 04/03/2020

## Add libraries
library(tidyverse)
library(ggfortify)

## Read in the data
movies <- read.csv("movies_final.csv")

## Get structure of data
str(movies)

## Get summary of data
summary(movies)

## Create subset of just Dramas
drama <- movies %>% filter(Primary.Genre == "Drama")

## Create subset of just Comedies
comedy <- movies %>% filter(Primary.Genre == "Comedy")

## Create subset of Comedies and Dramas to use for boxplot
dramcom <- movies %>% filter(Primary.Genre == "Drama" | Primary.Genre == "Comedy")

## Histogram for ratings of Comedies
ggplot(
  data = comedy,
  aes(
    x = rating
  )
) + 
  geom_histogram(
    bins = 7,   
    color = "black",
    fill = "lightblue"
  ) +
  labs(
    x = "Rating",
    y = "Count",
    title = "Ratings of Comedies"
  ) + 
  theme_classic( )

## Histogram for ratings of Dramas
ggplot(
  data = drama,
  aes(
    x = rating
  )
) + 
  geom_histogram(
    bins = 7,   
    color = "black",
    fill = "lightblue"
  ) +
  labs(
    x = "Rating",
    y = "Count",
    title = "Ratings of Dramas"
  ) + 
  theme_classic( )

## Create boxplot of ratings by genre for Comedies and Dramas
ggplot(
  data = dramcom,
  aes(
    x = Primary.Genre,
    y = rating,
    fill = Primary.Genre
  )
) + stat_boxplot(
  geom = "errorbar"
) + geom_boxplot(
  
) + labs(
  x = "Genre",
  y = "Rating",
  title = "Rating by Genre for Drama & Comedy"
) + theme_classic()

## Do 2-sample t-test
t.test(
  x = drama$rating,
  y = comedy$rating
)

## Create boxplot of Revenue by Genre for all Genres
ggplot(
  data = movies,
  aes(
    x = Primary.Genre,
    y = revenue,
    fill = Primary.Genre
  )
) + stat_boxplot(
  geom = "errorbar"
) + geom_boxplot(
  
) + labs(
  x = "Genre",
  y = "Revenue",
  title = "Revenue by Genre"
) + theme_classic(
  
) + theme(
  axis.text.x = element_text(angle = 90)
)

## Create ANOVA model
model <- aov(
  revenue ~ Primary.Genre,
  data = movies
)

## Create 4-plot of residuals
autoplot(model) + 
  theme_classic( )

## Create histogram of residuals
hist(resid(model))

## Get results from ANOVA
summary.aov(model)

## Tukey's HSD on model
TukeyHSD(model)

