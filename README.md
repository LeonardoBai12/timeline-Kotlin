# Timeline Assignment

A timeline visualization app for displaying events in an efficient, space-optimized layout with full CRUD functionality.

https://github.com/user-attachments/assets/5a520c99-926b-417b-b4af-8643932a452a

## Features

- **Timeline Visualization**: Events displayed in space-efficient lanes using a custom lane assignment algorithm
- **Event Management**: Full CRUD operations (Create, Read, Update, Delete) with intuitive UI
- **Search Functionality**: Real-time search with database queries
- **Swipe to Delete**: Gesture-based event deletion
- **Material Design 3**: Modern UI with floating action button and bottom sheet editing
- **Offline First**: Room database for local storage with reactive UI updates
- **Clean Architecture**: MVVM-VI pattern with proper separation of concerns
- **Multi-Module Architecture**: Organized into feature-based modules with convention plugins

## Time Spent

**Total: Approximately 3 hours and 50 minutes****

**Note: Unit tests were written post-submission as a learning exercise.**

## What I Like About My Implementation

### Architecture Excellence
- **Clean MVVM-VI Pattern**: Pure composables with proper separation of concerns
- **Multi-Module Architecture**: Clear separation with domain, data, design-system, and common modules
- **Reactive Programming**: Flow-based reactive UI with real-time database updates
- **Dependency Injection**: Complete Dagger Hilt setup with proper module organization
- **Convention Plugins**: Streamlined build configuration across modules
- **Design System**: Shared UI components to keep consistent experience throughout the application

### User Experience
- **Intuitive Interactions**: FAB for adding, tap to edit, swipe to delete
- **Immediate Feedback**: Optimistic UI updates with proper error handling
- **Modern Design**: Material Design 3 with consistent design system components
- **Responsive UI**: Shimmer loading states and smooth transitions

### Technical Implementation
- **Type Safety**: Sealed classes for events and states, ensuring compile-time safety
- **Performance**: Lazy loading with LazyColumn and efficient database queries
- **Scalability**: Modular architecture that can easily accommodate new features
- **Modular Design**: Each module has clear responsibilities and minimal coupling

## What I Would Change If Doing It Again

### Process Improvements
1. **Start Simpler**: Begin with a basic MVP and incrementally add features
2. **Design First**: Spend more time on UI/UX design before implementation
3. **More Screens:** Developing more screens would make the screen simpler - but it would cost me more time with navigation
4. **Use Detekt since the beggining:** I should have used Detekt auto correction tools to save time with code style issues

### Technical Decisions
1. **Simpler Initial Architecture**: Start with basic repository pattern to reach a MVP, then refactor to clean architecture
2. **Phased Implementation**:
   - Phase 1: Basic timeline display
   - Phase 2: Lane assignment algorithm
   - Phase 3: CRUD operations
   - Phase 4: Testing
3. **Progressive Enhancement**: Add Room DB and Dagger Hilt after core functionality works
4. **Module Strategy**: Start with single module, then extract features as they stabilize

### Time Management
- Reserve 30 minutes at the end for testing and polish
- Use more incremental commits to track progress
- Implement features in smaller, testable chunks

## Design Decisions

### Visual Inspiration
- **Microsoft Teams Timeline**: Used as primary inspiration for lane-based event display
- **Material Design Guidelines**: Followed for consistent UI patterns and accessibility

### Technical Choices
1. **Lane Assignment Algorithm**: Custom implementation over third-party libraries to meet assignment requirements
2. **Jetpack Compose**: Modern declarative UI for better maintainability
3. **Room Database**: Local-first approach for offline capability and performance
4. **MVVM-VI Pattern**: Event-driven architecture for predictable state management using SOLID principles
5. **Flow-based Reactive Programming**: For real-time UI updates and clean data flow
6. **Multi-Module Architecture**: Separation of concerns and improved build times
7. **Convention Plugins**: Consistent build configuration across modules

### UI/UX Decisions
- **Bottom Sheet for Editing**: Maintains context while providing full editing capabilities - I could save some time with this approach
- **Swipe to Delete**: Follows Android conventions for intuitive gesture interactions
- **Lane Labels**: Clear visual hierarchy with "Lane 1", "Lane 2" labels for easy comprehension
- **Search Integration**: Immediate filtering for better user experience with large datasets

## Testing Strategy (If I Had More Time)

### Completed Testing
- **Manual Testing**: Comprehensive testing on physical device
- **Unit Test Architecture**: Complete test suite for all layers (written post-submission)

### Test Coverage Goals
- **Domain Layer**: 100% coverage for use cases and business logic
- **Data Layer**: Repository and database service testing with mocked dependencies
- **Presentation Layer**: ViewModel state management and event handling
- **UI Layer**: Critical user journeys and error scenarios

### No Special Configuration Required
- The app uses standard Android build tools and dependencies
- All required dependencies are specified in module `build.gradle.kts` files
- Convention plugins handle common build configuration
- Room database is created automatically on first run
- No external services or API keys required

## Project Structure

This project uses a multi-module architecture with convention plugins for consistent build configuration:

```
settings.gradle.kts:
include(":app")
include(":libs:common") 
include(":libs:data")
include(":libs:design-system")
include(":libs:domain")
```

### Module Organization

```
:app/
└── src/main/kotlin/com/company/interview/schedule/
    ├── presentation/
    │   └── timeline/        # Timeline screen and ViewModel
    ├── MainActivity.kt
    └── ScheduleApplication.kt

:libs:common/
└── src/main/kotlin/io/lb/schedule/
    ├── model/              # Shared domain models
    └── util/               # Common utilities

:libs:data/
└── src/main/kotlin/io/lb/schedule/data/
    ├── dao/               # Room DAOs
    ├── database/          # Room database setup
    ├── datasource/        # Data source implementations
    ├── di/                # Data layer DI modules
    ├── model/             # Room entities
    ├── repository/        # Repository implementations
    └── service/           # Database services

:libs:design-system/
└── src/main/kotlin/io/lb/schedule/
    ├── components/        # Reusable UI components
    └── ui/
        └── theme/         # Material theming
            ├── Color.kt
            ├── Theme.kt
            └── Type.kt

:libs:domain/
└── src/main/kotlin/io/lb/schedule/domain/
    ├── di/                # Domain layer DI modules
    ├── repository/        # Repository interfaces
    └── usecase/           # Business logic use cases
```

### Module Dependencies

- **:app** → depends on all libs modules
- **:libs:data** → depends on :libs:domain, :libs:common (implements repository interfaces)
- **:libs:domain** → depends on :libs:common
- **:libs:design-system** → depends on :libs:common
- **:libs:common** → no dependencies (base module)

### Convention Plugins

The project uses Gradle convention plugins to standardize build configuration across modules:
- Common Android configuration
- Compose setup
- Hilt integration
- Testing dependencies
- Kotlin compiler options

## Key Technical Achievements

1. **Reactive Architecture**: Complete Flow-based reactive programming implementation
2. **Clean Architecture**: Proper dependency inversion with clear layer boundaries
3. **Modern Android**: Latest Jetpack Compose, Room, and Hilt implementation pattern
4. **Type Safety**: Comprehensive use of sealed classes and type-safe navigation
5. **Multi-Module Architecture**: Feature-based modules with clear separation of concerns
6. **Build Optimization**: Convention plugins for consistent and maintainable build scripts

## Learning Outcomes

This assignment reinforced the importance of:
- **Time-boxed Development**: Making pragmatic decisions under time constraints
- **Architecture Planning**: The value of upfront architectural decisions
- **Incremental Development**: Building in small, testable increments
- **Modular Design**: Benefits of feature-based modules for scalability and maintainability

---

*Built with Kotlin, Jetpack Compose, Room, and Dagger Hilt using multi-module architecture*
