package com.example.a7minsworkout

object Constants {

        fun defaultExerciseList(): ArrayList<ExerciseModel> {
            val exerciseList = ArrayList<ExerciseModel>()

            // Egzersizleri listeye ekliyoruz
            exerciseList.add(
                ExerciseModel(
                    1, "Stretch For Exercise",
                    R.drawable.ic_strach, false, false
                )
            )
            exerciseList.add(
                ExerciseModel(
                    2, "Squat Time",
                    R.drawable.ic_ex_squats, false, false
                )
            )
            exerciseList.add(
                ExerciseModel(
                    3, "Get To Training",
                    R.drawable.ic_training, false, false
                )
            )
            exerciseList.add(
                ExerciseModel(
                    4, "Zumba-Zumba-Zumba",
                    R.drawable.ic_zumba, false, false
                )
            )
            exerciseList.add(
                ExerciseModel(
                    5, "Plates For Fight",
                    R.drawable.ic_plates, false, false
                )
            )
            exerciseList.add(
                ExerciseModel(
                    6, "Go To Get Your Dumball",
                    R.drawable.ic_dumballs, false, false
                )
            )
            exerciseList.add(
                ExerciseModel(
                    7, "Can you balance?",
                    R.drawable.ic_balance, false, false
                )
            )
            exerciseList.add(
                ExerciseModel(
                    8, "Fists Forward",
                    R.drawable.ic_bodycombat, false, false
                )
            )
            exerciseList.add(
                ExerciseModel(
                    9, "Are you ready? To die.",
                    R.drawable.ic_deadlift, false, false
                )
            )
            exerciseList.add(
                ExerciseModel(
                    10, "Come on, Stronger. UP!!!",
                    R.drawable.ic_push_ups, false, false
                )
            )
            exerciseList.add(
                ExerciseModel(
                    11, "OK. Now let's go for a run.",
                    R.drawable.ic_treadmill, false, false
                )
            )
            exerciseList.add(
                ExerciseModel(
                    12, "You did great. Now get some rest.",
                    R.drawable.ic_yoga, false, false
                )
            )

            return exerciseList
        }
    }

