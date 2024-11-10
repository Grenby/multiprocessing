Запуски для замера скорости сортировки мерялись jmh. Каждый запуск сортировки это шафл + сортировка.


Ниже приведены замеры для:
1) однопоточной сортировки (quickSortSeqBenchMark)
2) многопоточной сортировки (quickSortPoolBenchMark)
3) шафла массива (shuffle)

QuickSortBenchMark.quickSortPoolBenchMark  avgt   20  13550.299 ± 459.397  ms/op
QuickSortBenchMark.quickSortSeqBenchMark   avgt   20  21427.141 ± 156.851  ms/op
QuickSortBenchMark.shuffle                 avgt   20  10144.268 ± 310.745  ms/op

Принебрегая всеми погрешностями
1) скорость однопоточной реализации 11 секунд на сортировку 10^8 элементов
2) скорость многопоточной реализации 3 секунды на сортировку 10^8 элементов

Следовательно ускорение примерно 3.6 раз.
Бенчмарк src/test/java/bit/multiprocessing/programming/QuickSortBenchMark.java
