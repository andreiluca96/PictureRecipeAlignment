using CSharpFunctionalExtensions;

namespace SSE.Infrastructure.Dispatchers
{
    public class CommandDispatcher : ICommandDispatcher
    {
        private readonly IDependencyScope scope;

        public CommandDispatcher(IDependencyScope scope)
        {
            this.scope = scope;
        }

        public Result Handle<TCommand>(TCommand command)
            where TCommand : class, ICommand
        {
            var handler = scope.Resolve<ICommandHandler<TCommand>>();
            return handler.Execute(command);
        }
    }
}
