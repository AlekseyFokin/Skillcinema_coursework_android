Создание мобильного приложения для поиска фильмов и сериалов, а также
управления коллекциями фильмов. Источник данных для приложения — сервис Kinopoisk Api Unofficial.

Техническое задание и экспортный pdf файл из Figma в папке ТЗ_Макет

Макет в самой Figma расшарен по
адресу https://www.figma.com/design/IIkauEDQXNEAbzLtBufGOj/skillcinema-(Copy)?m=auto&t=F5O6r2FkJLGH71jL-1

**Пояснения к выполненной работе:**

Загрузка на вкладку "Главная" (HomeFragment) 6 коллекций фильмов размером 20 штук: премьеры,
популярное, две динамические коллекции, зависящие от комбинации страна/жанр, которые выбираются
случайным образом, топ-25 и сериалы - тоже загружаются из существующего пресета в api.

<img src="https://github.com/AlekseyFokin/coursework_android/blob/master/GIFS/home.gif" alt="gif" width="360" height="800">

Как выяснилось, различные api дают похожую, но не одинаковую информацию о фильмах и ее приходится
приводить к общему отображаемому типу MovieRVModel, поэтому такое разнообразие классов в пакете
entities: премьеры, коллекции (топ-250, сериалы, популярные) и компиляции (зависят от страны и
жанра).

На HomeFragment установлен двухуровневый RecyclerView с неоднородными viewHoldерами, который
реализован с помощью getItemViewType. Идентификация вида viewHolder осуществляется по специальной
метке в классе модели.

Также добавлена возможность открытия отдельного фрагмента на каждый список фильмов. Так как список
премьер на 2 недели ограничен и уже загружен - передаю его целиком через bandle в новый фрагмент.

Остальные списки фильмов требуют paging lib, поэтому загружаются сначала, минуя те usecase классы,
которые использовались для в HomeFragment. Вся обработка полученных данных делается в классах
MoviePagingSource. А при создании коллекций без пагинации, напротив, приведение загруженных данных
осуществляется в соответствующих usecase классах.

Реализация загрузки списков фильмов в отдельные фрагменты осуществлена в пакете collections.
Отдельно реализация для премьер, отдельно с пагинацией, которая тоже разделена на коллекции и
компиляции. Последние два пакета очень похожи, но я посчитал что лучше так систематизировать, чем
потом запутаться.

Выбор - фрагмент с какой коллекцией/ компиляцией или премьерами загружать - выполняется в
HomeFragment - там обработка нажатия на кнопку "все" или "показать все". Дифференциация происходит
по стринговой метке, список которых лежит в KinopoiskApi.

Также добавил анимацию на открытие фрагментов со списками файлов на основе xml.

Я поработал над закладкой "Профиль": подключил базу данных, заполняю закладку сохраненными данными,
не из интернета. Доработал фрагмент с информацией об одном фильме OneMovieFragment. Реализовал все
функции меню на постере: добавление в разные коллекции (в бд): любимые фильмы, хочу посмотреть,
просмотренные, а также создание новых локальных коллекций.

База данных состоит из двух связанных таблиц. В одной хранятся записи коллекций, во второй
информация о фильмах. Фильмы могут принадлежать нескольким коллекциям, при этом записей с одним
фильмом будет столько, во скольких коллекциях он присутствует. В таблице фильмов хранятся пути к
постерам фильмов. Сами постеры (jpg файлы) сохраняются в директорию posters во внутренней памяти
приложения, поэтому разрешения у пользователя не просят. При удалении фильма из коллекции - файл с
постером тоже удаляется. Фильм, который был открыт в OneMovieFragment добавляется в коллекцию
фильмов, которыми пользователь интересовался, при этом размер этой коллекции - 20 фильмов, дубли не
добавляются, при превышении порога самая старая запись - удаляется. При создании БД в нее вносятся
предустановленные коллекции, которые нельзя потом удалить (поле embdded). Новые коллекции, которые
создает пользователь можно удалять вместе с фильмами в них хранящимися.

Наибольшую сложность вызвал механизм синхронизации фильмов которые попали в коллекцию просмотренных,
ведь они во всех RV должны сразу же получить метку в правом нижнем углу(глаз) и быть залиты
сине-прозрачным градиентом. Поэтому требуется передавать этот сигнал по обновлению rv в предыдущие
фрагменты(из бекстека). По - возможности во фрагментах обновлялись отдельные записи, но такое не
везде возможно, например во фрагменте HomeFragment один и тот же фильм вполне может быть сразу в
нескольких RV поэтому при получении этим фрагментом сигнала об изменении состояния фильма - он
полностью перезагружается, что естественно кажется расточительным. Для передачи таких сигналов об
обновлении состояния фильма использовал FragmentResult. В месте указания что фильм просмотрен
создается результат фрагмента, а в прочих фрагментах на функцию onResume вешается листнер
FragmentResult. Этот же листнер при необходимости прокидывает результат на следующий
восстанавливаемый из бекстека фрагмент.

Также выполнил BottomSheetDialogFragment на котором можно манипулировать
коллекциями и вставкой фильмов в эти коллекции, там и фрагмент, и диалоги, и RV в диалогах. В дизайн
проекте кнопка "Добавить в коллекцию" не совсем правильно называется, так как при снятии уже
существующей галочки с коллекции, фильм из этой коллекции удаляется.

Я реализовал закладку "Поиск". Опции поиска решил делать с разделяемой ViewModel. Столкнулся с тем,
что у меня не получается делать разделяемую ViewModel с помощью requireParentFragment(), так как
ViewModel требует зависимостей. Пришлось заводить HiltComponent и делать инжекцию
зависимости(usecase) в поле. Второе с чем столкнулся - не смог через стандартные компоненты сделать
YearPicker - компонент выбора года, в итоге сделал castomview с внутренней логикой.
