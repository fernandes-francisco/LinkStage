package turmaA.grupoB.LinkStage.viewmodel.chat

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import turmaA.grupoB.LinkStage.data.remote.model.application.ApplicationModel
import turmaA.grupoB.LinkStage.data.remote.model.communication.MessageModel
import turmaA.grupoB.LinkStage.data.remote.model.communication.MessageThreadModel
import turmaA.grupoB.LinkStage.data.remote.model.communication.MessageThreadParticipantModel
import turmaA.grupoB.LinkStage.data.remote.model.communication.NotificationModel
import turmaA.grupoB.LinkStage.data.remote.model.communication.SendMessageInput
import turmaA.grupoB.LinkStage.data.remote.model.enums.ApplicationStatus
import turmaA.grupoB.LinkStage.data.remote.model.enums.InternshipStatus
import turmaA.grupoB.LinkStage.data.remote.model.enums.OfferStatus
import turmaA.grupoB.LinkStage.data.remote.model.enums.UserRole
import turmaA.grupoB.LinkStage.data.remote.model.institution.InstitutionModel
import turmaA.grupoB.LinkStage.data.remote.model.internship.InternshipModel
import turmaA.grupoB.LinkStage.data.remote.model.offer.InternshipOfferModel
import turmaA.grupoB.LinkStage.data.remote.model.user.ProfileModel
import turmaA.grupoB.LinkStage.data.remote.model.user.StudentModel
import turmaA.grupoB.LinkStage.data.remote.model.user.SupervisorModel
import turmaA.grupoB.LinkStage.data.repository.application.ApplicationRepositoryInterface
import turmaA.grupoB.LinkStage.data.repository.auth.AuthRepositoryInterface
import turmaA.grupoB.LinkStage.data.repository.communication.CommunicationRepositoryInterface
import turmaA.grupoB.LinkStage.data.repository.institution.InstitutionRepositoryInterface
import turmaA.grupoB.LinkStage.data.repository.internship.InternshipRepositoryInterface
import turmaA.grupoB.LinkStage.data.repository.offer.OfferRepositoryInterface
import turmaA.grupoB.LinkStage.data.repository.profile.ProfileRepositoryInterface
import turmaA.grupoB.LinkStage.data.repository.student.StudentRepositoryInterface
import turmaA.grupoB.LinkStage.data.repository.supervisor.SupervisorRepositoryInterface
import turmaA.grupoB.LinkStage.data.remote.model.user.SupervisorSkillModel
import turmaA.grupoB.LinkStage.data.remote.model.auth.SignInInput
import turmaA.grupoB.LinkStage.data.remote.model.auth.SignUpInput
import turmaA.grupoB.LinkStage.data.remote.model.user.CreateStudentInput
import turmaA.grupoB.LinkStage.data.remote.model.user.UpdateProfileInput
import turmaA.grupoB.LinkStage.data.remote.model.internship.CreateActivityLogInput
import turmaA.grupoB.LinkStage.data.remote.model.internship.CreateInternshipInput
import turmaA.grupoB.LinkStage.data.remote.model.internship.ActivityLogModel
import turmaA.grupoB.LinkStage.data.remote.model.internship.AssignSupervisorInput
import turmaA.grupoB.LinkStage.data.remote.model.application.CreateApplicationInput
import turmaA.grupoB.LinkStage.data.remote.model.application.UpdateApplicationDecisionInput
import turmaA.grupoB.LinkStage.data.remote.model.offer.CreateOfferInput
import turmaA.grupoB.LinkStage.data.remote.model.offer.UpdateOfferInput

class ChatDataSourceTest {

    @Test
    fun instituicaoDataSourceShouldLoadInternshipAndApplicationThreads() = runTest {
        val auth = FakeAuthRepository("institution-user")
        val institution = InstitutionModel(
            id = "institution-1",
            userId = "institution-user",
            name = "Institution",
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val offer = InternshipOfferModel(
            id = "offer-1",
            institutionId = institution.id,
            title = "Offer",
            description = "Description",
            area = "IT",
            status = OfferStatus.PUBLISHED,
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val application = ApplicationModel(
            id = "application-1",
            offerId = offer.id,
            studentId = "student-1",
            status = ApplicationStatus.ACCEPTED,
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val internship = InternshipModel(
            id = "internship-1",
            applicationId = application.id,
            offerId = offer.id,
            studentId = "student-1",
            institutionId = institution.id,
            title = "Internship",
            status = InternshipStatus.IN_PROGRESS,
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val thread = MessageThreadModel(
            id = "thread-1",
            internshipId = internship.id,
            createdAt = "2026-01-01T00:00:00Z",
        )
        val applicationThread = MessageThreadModel(
            id = "thread-2",
            applicationId = application.id,
            createdAt = "2026-01-02T00:00:00Z",
        )
        val communication = FakeCommunicationRepository(
            internshipThreads = mapOf(internship.id to listOf(thread)),
            applicationThreads = mapOf(application.id to listOf(applicationThread)),
            participants = mapOf(
                thread.id to listOf(participant(thread.id, "student-1")),
                applicationThread.id to listOf(participant(applicationThread.id, "student-1")),
            ),
            messages = mapOf(
                thread.id to listOf(message(thread.id, "student-1", "Hello")),
                applicationThread.id to emptyList(),
            ),
        )
        val dataSource = InstituicaoChatDataSource(
            authRepository = auth,
            institutionRepository = FakeInstitutionRepository(institution),
            offerRepository = FakeOfferRepository(offer),
            internshipRepository = FakeInternshipRepository(listOf(internship)),
            applicationRepository = FakeApplicationRepository(mapOf(offer.id to application)),
            studentRepository = FakeStudentRepository(),
            profileRepository = FakeProfileRepository(),
            communicationRepository = communication,
        )

        val state = dataSource.loadConversations()

        assertEquals(2, state.conversations.size)
        assertEquals("thread-2", state.conversations.first().id)
        assertEquals("thread-1", state.conversations.last().id)
        assertEquals("Student student-1", state.conversations.first().name)
        assertEquals(1, state.contacts.size)
        assertEquals("student-1", state.contacts.first().id)
    }

    @Test
    fun chatMappersShouldUseFallbackNameAndLatestMessageWhenTimestampCannotParse() {
        val thread = MessageThreadModel(
            id = "thread-1",
            internshipId = "internship-1",
            createdAt = "2026-01-01T00:00:00Z",
        )
        val participant = participant(thread.id, "student-1")
        val messages = listOf(
            MessageModel(
                id = "old",
                threadId = thread.id,
                senderId = "student-1",
                content = "Old",
                isRead = true,
                createdAt = "not-a-date-1",
            ),
            MessageModel(
                id = "new",
                threadId = thread.id,
                senderId = "student-1",
                content = "New",
                isRead = true,
                createdAt = "not-a-date-2",
            ),
        )
        val profiles = mapOf("student-1" to FakeProfileRepository().studentProfile("student-1"))

        val conversation = ChatMappers.toConversation(
            thread = thread,
            participants = listOf(participant),
            messages = messages,
            profilesByUserId = profiles,
            currentUserId = "institution-user",
            fallbackName = "Student student-1",
        )

        assertEquals("Student student-1", conversation.name)
        assertEquals("New", conversation.lastMessage)
    }

    @Test
    fun instituicaoDataSourceShouldSendMessagesThroughCommunicationRepository() = runTest {
        val auth = FakeAuthRepository("institution-user")
        val communication = FakeCommunicationRepository()
        val dataSource = InstituicaoChatDataSource(
            authRepository = auth,
            institutionRepository = FakeInstitutionRepository(),
            offerRepository = FakeOfferRepository(),
            internshipRepository = FakeInternshipRepository(),
            applicationRepository = FakeApplicationRepository(),
            studentRepository = FakeStudentRepository(),
            profileRepository = FakeProfileRepository(),
            communicationRepository = communication,
        )

        dataSource.sendMessage("thread-1", "Hello")

        assertEquals(SendMessageInput("thread-1", "institution-user", "Hello"), communication.sentMessages.single())
    }

    @Test
    fun instituicaoDataSourceShouldMarkOtherUsersUnreadMessagesAsRead() = runTest {
        val auth = FakeAuthRepository("institution-user")
        val unreadStudentMessage = message("thread-1", "student-1", "Hello", isRead = false)
        val readStudentMessage = message("thread-1", "student-1", "Old", isRead = true)
        val ownMessage = message("thread-1", "institution-user", "Reply", isRead = false)
        val communication = FakeCommunicationRepository(
            messages = mapOf("thread-1" to listOf(unreadStudentMessage, readStudentMessage, ownMessage)),
        )
        val dataSource = InstituicaoChatDataSource(
            authRepository = auth,
            institutionRepository = FakeInstitutionRepository(),
            offerRepository = FakeOfferRepository(),
            internshipRepository = FakeInternshipRepository(),
            applicationRepository = FakeApplicationRepository(),
            studentRepository = FakeStudentRepository(),
            profileRepository = FakeProfileRepository(),
            communicationRepository = communication,
        )

        dataSource.markThreadMessagesAsRead("thread-1")

        assertEquals(listOf(unreadStudentMessage.id), communication.markedReadMessageIds)
    }

    @Test
    fun orientadorDataSourceShouldLoadSupervisorInternshipThreadsAndResolveStudentThread() = runTest {
        val auth = FakeAuthRepository("supervisor-user")
        val supervisor = SupervisorModel(
            id = "supervisor-1",
            userId = "supervisor-user",
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val internship = InternshipModel(
            id = "internship-1",
            applicationId = "application-1",
            offerId = "offer-1",
            studentId = "student-1",
            institutionId = "institution-1",
            supervisorId = supervisor.id,
            title = "Internship",
            status = InternshipStatus.IN_PROGRESS,
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val thread = MessageThreadModel(
            id = "thread-1",
            internshipId = internship.id,
            createdAt = "2026-01-01T00:00:00Z",
        )
        val communication = FakeCommunicationRepository(
            internshipThreads = mapOf(internship.id to listOf(thread)),
            participants = mapOf(thread.id to listOf(participant(thread.id, "student-1"))),
            messages = mapOf(thread.id to listOf(message(thread.id, "student-1", "Hello"))),
        )
        val dataSource = OrientadorChatDataSource(
            authRepository = auth,
            supervisorRepository = FakeSupervisorRepository(supervisor),
            internshipRepository = FakeInternshipRepository(listOf(internship), studentInternships = mapOf("student-1" to listOf(internship))),
            studentRepository = FakeStudentRepository(),
            profileRepository = FakeProfileRepository(),
            communicationRepository = communication,
        )

        val state = dataSource.loadConversations()
        val resolvedThread = dataSource.ensureThreadForStudent("student-1")

        assertEquals(1, state.conversations.size)
        assertEquals("thread-1", state.conversations.first().id)
        assertEquals("Student student-1", state.conversations.first().name)
        assertEquals("thread-1", resolvedThread?.threadId)
    }

    private fun participant(threadId: String, userId: String): MessageThreadParticipantModel = MessageThreadParticipantModel(
        id = "$threadId-$userId",
        threadId = threadId,
        userId = userId,
        createdAt = "2026-01-01T00:00:00Z",
    )

    private fun message(threadId: String, senderId: String, content: String, isRead: Boolean = true): MessageModel = MessageModel(
        id = "$threadId-message",
        threadId = threadId,
        senderId = senderId,
        content = content,
        isRead = isRead,
        createdAt = "2026-01-01T00:00:00Z",
    )

    private class FakeAuthRepository(private val userId: String) : AuthRepositoryInterface {
        val sentMessages = mutableListOf<SendMessageInput>()

        override suspend fun signUp(input: SignUpInput) = error("Not implemented")
        override suspend fun signIn(input: SignInInput) = Unit
        override suspend fun signOut() = Unit
        override fun getCurrentUserId(): String? = userId
        override fun isUserLoggedIn(): Boolean = userId.isNotBlank()
        override suspend fun getCurrentUserProfile(): ProfileModel? = null
        override suspend fun updateProfile(userId: String, input: UpdateProfileInput): ProfileModel = error("Not implemented")
    }

    private class FakeInstitutionRepository(private val institution: InstitutionModel? = null) : InstitutionRepositoryInterface {
        override suspend fun getInstitutions(): List<InstitutionModel> = institution?.let(::listOf).orEmpty()
        override suspend fun getInstitutionById(institutionId: String): InstitutionModel? = institution?.takeIf { it.id == institutionId }
        override suspend fun getInstitutionByUserId(userId: String): InstitutionModel? = institution?.takeIf { it.userId == userId }
    }

    private class FakeOfferRepository(private val offer: InternshipOfferModel? = null) : OfferRepositoryInterface {
        override suspend fun getPublishedOffers(): List<InternshipOfferModel> = offer?.let(::listOf).orEmpty()
        override suspend fun getOffersByInstitution(institutionId: String): List<InternshipOfferModel> = offer?.takeIf { it.institutionId == institutionId }?.let(::listOf).orEmpty()
        override suspend fun getOfferById(offerId: String): InternshipOfferModel? = offer?.takeIf { it.id == offerId }
        override suspend fun createOffer(input: CreateOfferInput): InternshipOfferModel = error("Not implemented")
        override suspend fun updateOffer(offerId: String, input: UpdateOfferInput): InternshipOfferModel = error("Not implemented")
        override suspend fun closeOffer(offerId: String): InternshipOfferModel = error("Not implemented")
        override suspend fun markOfferAsRemoved(offerId: String): InternshipOfferModel = error("Not implemented")
    }

    private class FakeApplicationRepository(private val applicationsByOffer: Map<String, ApplicationModel> = emptyMap()) : ApplicationRepositoryInterface {
        override suspend fun getApplicationById(applicationId: String): ApplicationModel? = applicationsByOffer.values.firstOrNull { it.id == applicationId }
        override suspend fun getApplicationsByOffer(offerId: String): List<ApplicationModel> = applicationsByOffer[offerId]?.let(::listOf).orEmpty()
        override suspend fun getApplicationsByStudent(studentId: String): List<ApplicationModel> = applicationsByOffer.values.filter { it.studentId == studentId }
        override suspend fun getApplicationsByStatus(status: ApplicationStatus): List<ApplicationModel> = applicationsByOffer.values.filter { it.status == status }
        override suspend fun createApplication(input: CreateApplicationInput): ApplicationModel = error("Not implemented")
        override suspend fun updateApplicationDecision(applicationId: String, input: UpdateApplicationDecisionInput): ApplicationModel = error("Not implemented")
        override suspend fun acceptApplication(applicationId: String): ApplicationModel = error("Not implemented")
        override suspend fun rejectApplication(applicationId: String, rejectionReason: String): ApplicationModel = error("Not implemented")
    }

    private class FakeInternshipRepository(
        private val internships: List<InternshipModel> = emptyList(),
        private val studentInternships: Map<String, List<InternshipModel>> = emptyMap(),
    ) : InternshipRepositoryInterface {
        override suspend fun getInternships(): List<InternshipModel> = internships
        override suspend fun getInternshipById(internshipId: String): InternshipModel? = internships.firstOrNull { it.id == internshipId }
        override suspend fun getInternshipsByStudent(studentId: String): List<InternshipModel> = studentInternships[studentId].orEmpty()
        override suspend fun getInternshipsByInstitution(institutionId: String): List<InternshipModel> = internships.filter { it.institutionId == institutionId }
        override suspend fun getInternshipsByStatus(status: InternshipStatus): List<InternshipModel> = internships.filter { it.status == status }
        override suspend fun createInternship(input: CreateInternshipInput): InternshipModel = error("Not implemented")
        override suspend fun assignSupervisor(internshipId: String, input: AssignSupervisorInput): InternshipModel = error("Not implemented")
        override suspend fun getActivityLogsByInternship(internshipId: String): List<ActivityLogModel> = emptyList()
        override suspend fun createActivityLog(input: CreateActivityLogInput): ActivityLogModel = error("Not implemented")
    }

    private class FakeStudentRepository : StudentRepositoryInterface {
        private val students = listOf(
            StudentModel(
                id = "student-1",
                userId = "student-1",
                studentNumber = "12345",
                course = "Course",
                createdAt = "2026-01-01T00:00:00Z",
                updatedAt = "2026-01-01T00:00:00Z",
            )
        )

        override suspend fun getStudents(): List<StudentModel> = students
        override suspend fun getStudentById(studentId: String): StudentModel? = students.firstOrNull { it.id == studentId }
        override suspend fun getStudentByUserId(userId: String): StudentModel? = students.firstOrNull { it.userId == userId }
        override suspend fun getStudentByNumber(studentNumber: String): StudentModel? = null
        override suspend fun getStudentsByCourse(course: String): List<StudentModel> = students
        override suspend fun createStudent(input: CreateStudentInput): StudentModel = error("Not implemented")
    }

    private class FakeSupervisorRepository(private val supervisor: SupervisorModel? = null) : SupervisorRepositoryInterface {
        override suspend fun getSupervisors(): List<SupervisorModel> = supervisor?.let(::listOf).orEmpty()
        override suspend fun getSupervisorById(supervisorId: String): SupervisorModel? = supervisor?.takeIf { it.id == supervisorId }
        override suspend fun getSupervisorByUserId(userId: String): SupervisorModel? = supervisor?.takeIf { it.userId == userId }
        override suspend fun getAvailableSupervisors(): List<SupervisorModel> = supervisor?.let(::listOf).orEmpty()
        override suspend fun getSupervisorsByDepartment(department: String): List<SupervisorModel> = supervisor?.let(::listOf).orEmpty()
        override suspend fun getSupervisorSkills(supervisorId: String): List<SupervisorSkillModel> = emptyList()
    }

    private class FakeProfileRepository : ProfileRepositoryInterface {
        override suspend fun getProfiles(): List<ProfileModel> = listOf(studentProfile("student-1"))
        override suspend fun getProfileById(userId: String): ProfileModel? = studentProfile(userId).takeIf { it.id == userId }
        override suspend fun getProfilesByRole(role: String): List<ProfileModel> = if (role == UserRole.STUDENT.name) getProfiles() else emptyList()

        internal fun studentProfile(userId: String): ProfileModel = ProfileModel(
            id = userId,
            name = "Student $userId",
            email = "$userId@example.com",
            role = UserRole.STUDENT,
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
    }

    private class FakeCommunicationRepository(
        private val internshipThreads: Map<String, List<MessageThreadModel>> = emptyMap(),
        private val applicationThreads: Map<String, List<MessageThreadModel>> = emptyMap(),
        private val participants: Map<String, List<MessageThreadParticipantModel>> = emptyMap(),
        private val messages: Map<String, List<MessageModel>> = emptyMap(),
    ) : CommunicationRepositoryInterface {
        val sentMessages = mutableListOf<SendMessageInput>()
        val markedReadMessageIds = mutableListOf<String>()

        override suspend fun getNotificationsByUser(userId: String): List<NotificationModel> = emptyList()
        override suspend fun getUnreadNotificationsByUser(userId: String): List<NotificationModel> = emptyList()
        override suspend fun markNotificationAsRead(notificationId: String): NotificationModel? = null
        override suspend fun getThreadById(threadId: String): MessageThreadModel? = null
        override suspend fun getThreadsByInternship(internshipId: String): List<MessageThreadModel> = internshipThreads[internshipId].orEmpty()
        override suspend fun getThreadByApplication(applicationId: String): List<MessageThreadModel> = applicationThreads[applicationId].orEmpty()

        override suspend fun getThreadsWithParticipants(userIds: List<String>): List<MessageThreadModel> {
            val wanted = userIds.toSet()
            return participants
                .filterValues { threadParticipants ->
                    val actual = threadParticipants.map { it.userId }.toSet()
                    wanted.all { it in actual }
                }
                .keys
                .mapNotNull { threadId ->
                    internshipThreads.values.flatten().firstOrNull { it.id == threadId }
                        ?: applicationThreads.values.flatten().firstOrNull { it.id == threadId }
                }
                .distinctBy { it.id }
        }

        override suspend fun getParticipantsByThread(threadId: String): List<MessageThreadParticipantModel> = participants[threadId].orEmpty()
        override suspend fun getMessagesByThread(threadId: String): List<MessageModel> = messages[threadId].orEmpty()
        override suspend fun sendMessage(input: SendMessageInput): MessageModel? {
            sentMessages += input
            return null
        }
        override suspend fun markMessageAsRead(messageId: String): MessageModel? {
            markedReadMessageIds += messageId
            return messages.values.flatten().firstOrNull { it.id == messageId }
        }

        override suspend fun createThread(
            internshipId: String?,
            applicationId: String?
        ): MessageThreadModel? {
            var counter = 0
            while (true) {
                val candidate = MessageThreadModel(
                    id = "new-thread-$counter",
                    internshipId = internshipId,
                    applicationId = applicationId,
                    createdAt = "2026-06-13T00:00:00Z",
                )
                if (internshipThreads.values.flatten().none { it.id == candidate.id } &&
                    applicationThreads.values.flatten().none { it.id == candidate.id }) {
                    return candidate
                }
                counter++
            }
        }

        override suspend fun createThreadParticipant(
            threadId: String,
            userId: String
        ): MessageThreadParticipantModel = MessageThreadParticipantModel(
            id = "$threadId-$userId",
            threadId = threadId,
            userId = userId,
            createdAt = "2026-01-01T00:00:00Z",
        )
    }

    @Test
    fun instituicaoEnsureThreadShouldReturnExistingApplicationThread() = runTest {
        val auth = FakeAuthRepository("institution-user")
        val institution = InstitutionModel(
            id = "institution-1",
            userId = "institution-user",
            name = "Institution",
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val offer = InternshipOfferModel(
            id = "offer-1",
            institutionId = institution.id,
            title = "Offer",
            description = "Description",
            area = "IT",
            status = OfferStatus.PUBLISHED,
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val application = ApplicationModel(
            id = "application-1",
            offerId = offer.id,
            studentId = "student-1",
            status = ApplicationStatus.ACCEPTED,
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val thread = MessageThreadModel(
            id = "thread-1",
            applicationId = application.id,
            createdAt = "2026-01-01T00:00:00Z",
        )
        val communication = FakeCommunicationRepository(
            applicationThreads = mapOf(application.id to listOf(thread)),
        )
        val dataSource = InstituicaoChatDataSource(
            authRepository = auth,
            institutionRepository = FakeInstitutionRepository(institution),
            offerRepository = FakeOfferRepository(offer),
            internshipRepository = FakeInternshipRepository(),
            applicationRepository = FakeApplicationRepository(mapOf(offer.id to application)),
            studentRepository = FakeStudentRepository(),
            profileRepository = FakeProfileRepository(),
            communicationRepository = communication,
        )

        val result = dataSource.ensureThreadForStudent("student-1")

        assertNotNull(result)
        assertEquals("thread-1", result!!.threadId)
        assertFalse(result.created)
    }

    @Test
    fun instituicaoEnsureThreadShouldReturnExistingInternshipThread() = runTest {
        val auth = FakeAuthRepository("institution-user")
        val institution = InstitutionModel(
            id = "institution-1",
            userId = "institution-user",
            name = "Institution",
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val offer = InternshipOfferModel(
            id = "offer-1",
            institutionId = institution.id,
            title = "Offer",
            description = "Description",
            area = "IT",
            status = OfferStatus.PUBLISHED,
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val internship = InternshipModel(
            id = "internship-1",
            applicationId = "application-1",
            offerId = offer.id,
            studentId = "student-1",
            institutionId = institution.id,
            title = "Internship",
            status = InternshipStatus.IN_PROGRESS,
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val thread = MessageThreadModel(
            id = "thread-2",
            internshipId = internship.id,
            createdAt = "2026-01-01T00:00:00Z",
        )
        val communication = FakeCommunicationRepository(
            internshipThreads = mapOf(internship.id to listOf(thread)),
        )
        val dataSource = InstituicaoChatDataSource(
            authRepository = auth,
            institutionRepository = FakeInstitutionRepository(institution),
            offerRepository = FakeOfferRepository(offer),
            internshipRepository = FakeInternshipRepository(listOf(internship)),
            applicationRepository = FakeApplicationRepository(),
            studentRepository = FakeStudentRepository(),
            profileRepository = FakeProfileRepository(),
            communicationRepository = communication,
        )

        val result = dataSource.ensureThreadForStudent("student-1")

        assertNotNull(result)
        assertEquals("thread-2", result!!.threadId)
        assertFalse(result.created)
    }

    @Test
    fun instituicaoEnsureThreadShouldCreateNewThreadWhenNoneExists() = runTest {
        val auth = FakeAuthRepository("institution-user")
        val institution = InstitutionModel(
            id = "institution-1",
            userId = "institution-user",
            name = "Institution",
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val offer = InternshipOfferModel(
            id = "offer-1",
            institutionId = institution.id,
            title = "Offer",
            description = "Description",
            area = "IT",
            status = OfferStatus.PUBLISHED,
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val application = ApplicationModel(
            id = "application-1",
            offerId = offer.id,
            studentId = "student-1",
            status = ApplicationStatus.ACCEPTED,
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val communication = FakeCommunicationRepository()
        val dataSource = InstituicaoChatDataSource(
            authRepository = auth,
            institutionRepository = FakeInstitutionRepository(institution),
            offerRepository = FakeOfferRepository(offer),
            internshipRepository = FakeInternshipRepository(),
            applicationRepository = FakeApplicationRepository(mapOf(offer.id to application)),
            studentRepository = FakeStudentRepository(),
            profileRepository = FakeProfileRepository(),
            communicationRepository = communication,
        )

        val result = dataSource.ensureThreadForStudent("student-1")

        assertNotNull(result)
        assertEquals("new-thread-0", result!!.threadId)
        assertTrue(result.created)
    }

    @Test
    fun instituicaoEnsureThreadShouldNotReturnThreadFromAnotherStudent() = runTest {
        val auth = FakeAuthRepository("institution-user")
        val institution = InstitutionModel(
            id = "institution-1",
            userId = "institution-user",
            name = "Institution",
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val offer = InternshipOfferModel(
            id = "offer-1",
            institutionId = institution.id,
            title = "Offer",
            description = "Description",
            area = "IT",
            status = OfferStatus.PUBLISHED,
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val applicationA = ApplicationModel(
            id = "application-A",
            offerId = offer.id,
            studentId = "student-A",
            status = ApplicationStatus.ACCEPTED,
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val threadA = MessageThreadModel(
            id = "thread-A",
            applicationId = applicationA.id,
            createdAt = "2026-01-01T00:00:00Z",
        )
        val communication = FakeCommunicationRepository(
            applicationThreads = mapOf(applicationA.id to listOf(threadA)),
        )
        val dataSource = InstituicaoChatDataSource(
            authRepository = auth,
            institutionRepository = FakeInstitutionRepository(institution),
            offerRepository = FakeOfferRepository(offer),
            internshipRepository = FakeInternshipRepository(),
            applicationRepository = FakeApplicationRepository(mapOf(offer.id to applicationA)),
            studentRepository = FakeStudentRepository(),
            profileRepository = FakeProfileRepository(),
            communicationRepository = communication,
        )

        // Student B has no application with this institution, so thread from student A should not be returned
        val result = dataSource.ensureThreadForStudent("student-B")

        assertNull(result)
    }

    @Test
    fun orientadorEnsureThreadShouldReturnExistingStudentThread() = runTest {
        val auth = FakeAuthRepository("supervisor-user")
        val supervisor = SupervisorModel(
            id = "supervisor-1",
            userId = "supervisor-user",
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val internship = InternshipModel(
            id = "internship-1",
            applicationId = "application-1",
            offerId = "offer-1",
            studentId = "student-1",
            institutionId = "institution-1",
            supervisorId = supervisor.id,
            title = "Internship",
            status = InternshipStatus.IN_PROGRESS,
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val thread = MessageThreadModel(
            id = "thread-1",
            internshipId = internship.id,
            createdAt = "2026-01-01T00:00:00Z",
        )
        val communication = FakeCommunicationRepository(
            internshipThreads = mapOf(internship.id to listOf(thread)),
        )
        val dataSource = OrientadorChatDataSource(
            authRepository = auth,
            supervisorRepository = FakeSupervisorRepository(supervisor),
            internshipRepository = FakeInternshipRepository(
                listOf(internship),
                studentInternships = mapOf("student-1" to listOf(internship)),
            ),
            studentRepository = FakeStudentRepository(),
            profileRepository = FakeProfileRepository(),
            communicationRepository = communication,
        )

        val result = dataSource.ensureThreadForStudent("student-1")

        assertNotNull(result)
        assertEquals("thread-1", result!!.threadId)
        assertFalse(result.created)
    }

    @Test
    fun orientadorEnsureThreadShouldCreateNewThreadWhenNoneExists() = runTest {
        val auth = FakeAuthRepository("supervisor-user")
        val supervisor = SupervisorModel(
            id = "supervisor-1",
            userId = "supervisor-user",
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val internship = InternshipModel(
            id = "internship-1",
            applicationId = "application-1",
            offerId = "offer-1",
            studentId = "student-1",
            institutionId = "institution-1",
            supervisorId = supervisor.id,
            title = "Internship",
            status = InternshipStatus.IN_PROGRESS,
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val communication = FakeCommunicationRepository()
        val dataSource = OrientadorChatDataSource(
            authRepository = auth,
            supervisorRepository = FakeSupervisorRepository(supervisor),
            internshipRepository = FakeInternshipRepository(
                listOf(internship),
                studentInternships = mapOf("student-1" to listOf(internship)),
            ),
            studentRepository = FakeStudentRepository(),
            profileRepository = FakeProfileRepository(),
            communicationRepository = communication,
        )

        val result = dataSource.ensureThreadForStudent("student-1")

        assertNotNull(result)
        assertEquals("new-thread-0", result!!.threadId)
        assertTrue(result.created)
    }

    @Test
    fun orientadorEnsureThreadShouldNotReturnThreadFromAnotherSupervisor() = runTest {
        val auth = FakeAuthRepository("supervisor-A")
        val supervisorA = SupervisorModel(
            id = "supervisor-A",
            userId = "supervisor-A",
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val internshipForSupervisorB = InternshipModel(
            id = "internship-1",
            applicationId = "application-1",
            offerId = "offer-1",
            studentId = "student-1",
            institutionId = "institution-1",
            supervisorId = "supervisor-B",
            title = "Internship",
            status = InternshipStatus.IN_PROGRESS,
            createdAt = "2026-01-01T00:00:00Z",
            updatedAt = "2026-01-01T00:00:00Z",
        )
        val communication = FakeCommunicationRepository()
        val dataSource = OrientadorChatDataSource(
            authRepository = auth,
            supervisorRepository = FakeSupervisorRepository(supervisorA),
            internshipRepository = FakeInternshipRepository(
                listOf(internshipForSupervisorB),
                studentInternships = mapOf("student-1" to listOf(internshipForSupervisorB)),
            ),
            studentRepository = FakeStudentRepository(),
            profileRepository = FakeProfileRepository(),
            communicationRepository = communication,
        )

        val result = dataSource.ensureThreadForStudent("student-1")

        assertNull(result)
    }
}
